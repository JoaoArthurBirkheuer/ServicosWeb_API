package com.servicosweb.apirest.joao.controllers;

import com.servicosweb.apirest.joao.dtos.estacao.EstacaoRequestDTO;
import com.servicosweb.apirest.joao.dtos.estacao.EstacaoResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.services.EstacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estacoes")
public class EstacaoController {

    private final EstacaoService estacaoService;

    public EstacaoController(EstacaoService estacaoService) {
        this.estacaoService = estacaoService;
    }

    @GetMapping
    public ResponseEntity<List<EstacaoResponseDTO>> listarAtivas() {
        return ResponseEntity.ok(estacaoService.listarAtivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstacaoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(estacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstacaoResponseDTO> criar(
            @RequestBody @Valid EstacaoRequestDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        EstacaoResponseDTO response = estacaoService.criar(dto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EstacaoRequestDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(estacaoService.atualizar(id, dto, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        estacaoService.desativar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
