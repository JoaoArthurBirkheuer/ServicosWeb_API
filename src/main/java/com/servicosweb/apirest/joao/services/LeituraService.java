package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.leitura.LeituraRequestDTO;
import com.servicosweb.apirest.joao.dtos.leitura.LeituraResponseDTO;
import com.servicosweb.apirest.joao.entities.Estacao;
import com.servicosweb.apirest.joao.entities.Leitura;
import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
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
    public LeituraResponseDTO salvar(LeituraRequestDTO dto) {
        Estacao estacao = estacaoRepository.findById(dto.estacaoId())
                .orElseThrow(() -> new RuntimeException("Estação não encontrada."));

        Leitura leitura = Leitura.builder()
                .estacao(estacao)
                .timestampLeitura(dto.timestampLeitura())
                .temperatura(dto.temperatura())
                .umidade(dto.umidade())
                .pressao(dto.pressao())
                .velocidadeVento(dto.velocidadeVento())
                .direcaoVento(dto.direcaoVento())
                .precipitacao(dto.precipitacao())
                .qualidade(dto.qualidade())
                .build();

        return mapToResponse(leituraRepository.save(leitura));
    }

    public List<LeituraResponseDTO> listarPorEstacao(Long estacaoId, QualidadeLeitura qualidade) {
        List<Leitura> leituras;
        if (qualidade != null) {
            leituras = leituraRepository.findAllByEstacaoIdAndQualidade(estacaoId, qualidade);
        } else {
            leituras = leituraRepository.findAllByEstacaoId(estacaoId);
        }
        return leituras.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private LeituraResponseDTO mapToResponse(Leitura l) {
        return new LeituraResponseDTO(
                l.getTimestampLeitura(),
                l.getTemperatura(),
                l.getUmidade(),
                l.getPressao(),
                l.getVelocidadeVento(),
                l.getDirecaoVento(),
                l.getPrecipitacao(),
                l.getQualidade()
        );
    }
}