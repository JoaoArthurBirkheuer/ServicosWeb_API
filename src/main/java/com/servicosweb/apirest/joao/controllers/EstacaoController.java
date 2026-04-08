package com.servicosweb.apirest.joao.controllers;

import com.servicosweb.apirest.joao.dtos.estacao.EstacaoRequestDTO;
import com.servicosweb.apirest.joao.dtos.estacao.EstacaoResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.services.EstacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estações")
@RestController
@RequestMapping("/estacoes")
public class EstacaoController {

    private final EstacaoService estacaoService;

    public EstacaoController(EstacaoService estacaoService) {
        this.estacaoService = estacaoService;
    }

    @Operation(summary = "Lista todas as estações ativas")
    @GetMapping
    public ResponseEntity<List<EstacaoResponseDTO>> listarAtivas() {
        return ResponseEntity.ok(estacaoService.listarAtivas());
    }

    @Operation(summary = "Busca estação por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<EstacaoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(estacaoService.buscarPorId(id));
    }

    @Operation(summary = "Cria uma nova estação", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<EstacaoResponseDTO> criar(@RequestBody @Valid EstacaoRequestDTO dto, @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        EstacaoResponseDTO response = estacaoService.criarComEmail(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Atualiza uma estação", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<EstacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EstacaoRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(estacaoService.atualizar(id, dto, usuario));
    }

    @Operation(summary = "Desativa uma estação (Soft Delete)", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        estacaoService.desativar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
