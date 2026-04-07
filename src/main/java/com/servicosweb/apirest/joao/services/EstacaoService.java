package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.estacao.EstacaoRequestDTO;
import com.servicosweb.apirest.joao.dtos.estacao.EstacaoResponseDTO;
import com.servicosweb.apirest.joao.entities.Estacao;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.exceptions.AcessoNegadoException;
import com.servicosweb.apirest.joao.exceptions.RecursoNaoEncontradoException;
import com.servicosweb.apirest.joao.exceptions.RegraDeNegocioException;
import com.servicosweb.apirest.joao.repositories.EstacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstacaoService {

    private final EstacaoRepository estacaoRepository;

    public EstacaoService(EstacaoRepository estacaoRepository) {
        this.estacaoRepository = estacaoRepository;
    }

    public List<EstacaoResponseDTO> listarAtivas() {
        return estacaoRepository.findAllByAtivaTrue().stream()
                .map(this::mapToResponse).toList();
    }

    public EstacaoResponseDTO buscarPorId(Long id) {
        return mapToResponse(buscarEntidade(id));
    }

    @Transactional
    public EstacaoResponseDTO criar(EstacaoRequestDTO dto, Usuario usuario) {
        if (estacaoRepository.existsByCodigo(dto.codigo())) {
            throw new RegraDeNegocioException("Código de estação já em uso: " + dto.codigo());
        }
        Estacao estacao = Estacao.builder()
                .nome(dto.nome())
                .codigo(dto.codigo())
                .metadata(dto.metadata())
                .usuario(usuario)
                .build();
        return mapToResponse(estacaoRepository.save(estacao));
    }

    @Transactional
    public EstacaoResponseDTO atualizar(Long id, EstacaoRequestDTO dto, Usuario executor) {
        Estacao estacao = buscarEntidade(id);
        verificarPropriedade(estacao, executor);
        estacao.setNome(dto.nome());
        estacao.setMetadata(dto.metadata());
        return mapToResponse(estacaoRepository.save(estacao));
    }

    @Transactional
    public void desativar(Long id, Usuario executor) {
        Estacao estacao = estacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estação não encontrada."));
        verificarPropriedade(estacao, executor);
        estacao.setAtiva(false);
        estacaoRepository.save(estacao);
    }

    private Estacao buscarEntidade(Long id) {
        return estacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estação não encontrada: " + id));
    }

    private void verificarPropriedade(Estacao estacao, Usuario executor) {
        boolean ehDono = estacao.getUsuario().getId().equals(executor.getId());
        boolean ehAdmin = executor.getRole() == Role.ADMIN;
        if (!ehDono && !ehAdmin) {
            throw new AcessoNegadoException("Você não tem permissão para modificar esta estação.");
        }
    }

    private EstacaoResponseDTO mapToResponse(Estacao e) {
        return new EstacaoResponseDTO(e.getNome(), e.getCodigo(), e.getAtiva(),
                e.getMetadata(), e.getUsuario().getEmail(), e.getCreatedAt());
    }
}