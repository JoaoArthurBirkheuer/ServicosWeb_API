package com.servicosweb.apirest.joao.controllers;

import com.servicosweb.apirest.joao.dtos.leitura.LeituraRequestDTO;
import com.servicosweb.apirest.joao.dtos.leitura.LeituraResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import com.servicosweb.apirest.joao.services.LeituraService;
import com.servicosweb.apirest.joao.services.UsuarioService;
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
@Tag(name = "Leituras")
@RestController
@RequestMapping("/estacoes/{estacaoId}/leituras")
public class LeituraController {

    private final LeituraService leituraService;
    private final UsuarioService usuarioService;

    public LeituraController(LeituraService leituraService, UsuarioService usuarioService) {
        this.leituraService = leituraService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Lista leituras de uma estação", description = "Permite filtrar por qualidade (OK, SUSPEITO, ERRO).")
    @GetMapping
    public ResponseEntity<List<LeituraResponseDTO>> listar(@PathVariable Long estacaoId, @RequestParam(required = false) QualidadeLeitura qualidade) {
        return ResponseEntity.ok(leituraService.listarPorEstacao(estacaoId, qualidade));
    }

    @Operation(summary = "Obtém a leitura mais recente")
    @GetMapping("/recente")
    public ResponseEntity<LeituraResponseDTO> buscarUltimaLeitura(@PathVariable Long estacaoId) {
        return ResponseEntity.ok(leituraService.buscarUltima(estacaoId));
    }

    @Operation(summary = "Registra novos dados climáticos", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<LeituraResponseDTO> registrar(
            @PathVariable Long estacaoId,
            @RequestBody @Valid LeituraRequestDTO dto,
            @AuthenticationPrincipal Jwt jwt) {
        String emailLogado = jwt.getClaimAsString("email");

        Usuario usuario = usuarioService.buscarEntidadePorEmail(emailLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(leituraService.salvar(estacaoId, dto, usuario));
    }

    @Operation(summary = "Deleta uma leitura", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{leituraId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long estacaoId,
            @PathVariable Long leituraId,
            @AuthenticationPrincipal Jwt jwt) {

        String emailLogado = jwt.getClaimAsString("email");
        Usuario usuario = usuarioService.buscarEntidadePorEmail(emailLogado);

        leituraService.deletar(estacaoId, leituraId, usuario);
        return ResponseEntity.noContent().build();
    }
}