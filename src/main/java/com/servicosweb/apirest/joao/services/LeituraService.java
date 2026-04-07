package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.leitura.LeituraRequestDTO;
import com.servicosweb.apirest.joao.dtos.leitura.LeituraResponseDTO;
import com.servicosweb.apirest.joao.entities.Estacao;
import com.servicosweb.apirest.joao.entities.Leitura;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.exceptions.AcessoNegadoException;
import com.servicosweb.apirest.joao.exceptions.RecursoNaoEncontradoException;
import com.servicosweb.apirest.joao.repositories.EstacaoRepository;
import com.servicosweb.apirest.joao.repositories.LeituraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeituraService {

    private final LeituraRepository leituraRepository;
    private final EstacaoRepository estacaoRepository;

    public LeituraService(LeituraRepository leituraRepository, EstacaoRepository estacaoRepository) {
        this.leituraRepository = leituraRepository;
        this.estacaoRepository = estacaoRepository;
    }

    @Transactional
    public LeituraResponseDTO salvar(Long estacaoId, LeituraRequestDTO dto, Usuario usuario) {
        Estacao estacao = estacaoRepository.findById(estacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estação não encontrada."));

        validarPropriedade(estacao, usuario);

        Leitura leitura = Leitura.builder()
                .estacao(estacao)
                .timestampLeitura(dto.timestampLeitura())
                .temperatura(dto.temperatura())
                .umidade(dto.umidade())
                .pressao(dto.pressao())
                .velocidadeVento(dto.velocidadeVento())
                .direcaoVento(dto.direcaoVento())
                .precipitacao(dto.precipitacao())
                .qualidade(dto.qualidade() != null ? dto.qualidade() : QualidadeLeitura.OK)
                .build();

        return mapToResponse(leituraRepository.save(leitura));
    }

    @Transactional
    public void deletar(Long estacaoId, Long leituraId, Usuario usuario) {
        Leitura leitura = leituraRepository.findByIdAndEstacaoId(leituraId, estacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitura não encontrada para esta estação."));

        validarPropriedade(leitura.getEstacao(), usuario);

        leituraRepository.delete(leitura);
    }

    public List<LeituraResponseDTO> listarPorEstacao(Long estacaoId, QualidadeLeitura qualidade) {
        List<Leitura> leituras = (qualidade != null)
                ? leituraRepository.findAllByEstacaoIdAndQualidade(estacaoId, qualidade)
                : leituraRepository.findAllByEstacaoId(estacaoId);

        return leituras.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private void validarPropriedade(Estacao estacao, Usuario usuario) {
        boolean ehDono = estacao.getUsuario().getId().equals(usuario.getId());
        boolean ehAdmin = usuario.getRole() == Role.ADMIN;
        if (!ehDono && !ehAdmin) {
            throw new AcessoNegadoException("Sem permissão para operar nesta estação.");
        }
    }

    private LeituraResponseDTO mapToResponse(Leitura l) {
        return new LeituraResponseDTO(
                l.getTimestampLeitura(), l.getTemperatura(), l.getUmidade(),
                l.getPressao(), l.getVelocidadeVento(), l.getDirecaoVento(),
                l.getPrecipitacao(), l.getQualidade()
        );
    }

    public LeituraResponseDTO buscarUltima(Long estacaoId) {
        Leitura leitura = leituraRepository.findFirstByEstacaoIdOrderByTimestampLeituraDesc(estacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma leitura encontrada para esta estação."));
        return mapToResponse(leitura);
    }
}