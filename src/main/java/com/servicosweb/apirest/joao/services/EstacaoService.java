package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.estacao.EstacaoResponseDTO;
import com.servicosweb.apirest.joao.entities.Estacao;
import com.servicosweb.apirest.joao.repositories.EstacaoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstacaoService {

    private final EstacaoRepository estacaoRepository;

    public EstacaoService(EstacaoRepository estacaoRepository) {
        this.estacaoRepository = estacaoRepository;
    }

    public List<EstacaoResponseDTO> listarAtivas() {
        return estacaoRepository.findAllByAtivaTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private EstacaoResponseDTO mapToResponse(Estacao e) {
        return new EstacaoResponseDTO(
                e.getNome(),
                e.getCodigo(),
                e.getAtiva(),
                e.getMetadata(),
                e.getUsuario().getEmail(),
                e.getCreatedAt()
        );
    }
}