package com.servicosweb.apirest.joao.controllers;

import com.servicosweb.apirest.joao.dtos.leitura.LeituraRequestDTO;
import com.servicosweb.apirest.joao.dtos.leitura.LeituraResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import com.servicosweb.apirest.joao.services.LeituraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estacoes/{estacaoId}/leituras")
public class LeituraController {

    private final LeituraService leituraService;

    public LeituraController(LeituraService leituraService) {
        this.leituraService = leituraService;
    }

    @GetMapping
    public ResponseEntity<List<LeituraResponseDTO>> listar(
            @PathVariable Long estacaoId,
            @RequestParam(required = false) QualidadeLeitura qualidade) {
        return ResponseEntity.ok(leituraService.listarPorEstacao(estacaoId, qualidade));
    }

    @PostMapping
    public ResponseEntity<LeituraResponseDTO> registrar(
            @PathVariable Long estacaoId,
            @RequestBody @Valid LeituraRequestDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(leituraService.salvar(estacaoId, dto, usuario));
    }

    @DeleteMapping("/{leituraId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long estacaoId,
            @PathVariable Long leituraId,
            @AuthenticationPrincipal Usuario usuario) {
        leituraService.deletar(estacaoId, leituraId, usuario);
        return ResponseEntity.noContent().build();
    }
}