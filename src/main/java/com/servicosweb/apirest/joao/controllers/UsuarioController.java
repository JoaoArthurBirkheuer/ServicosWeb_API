package com.servicosweb.apirest.joao.controllers;

import com.servicosweb.apirest.joao.dtos.usuario.UsuarioRequestDTO;
import com.servicosweb.apirest.joao.dtos.usuario.UsuarioResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Perfil e Usuário")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtém dados do usuário logado", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obterMeuPerfil(@AuthenticationPrincipal Usuario logado) {
        return ResponseEntity.ok(new UsuarioResponseDTO(logado.getNome(), logado.getEmail(), logado.getRole(), logado.getCreatedAt()));
    }

    @Operation(summary = "Atualiza dados do próprio perfil", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> atualizarMeuPerfil(@RequestBody @Valid UsuarioRequestDTO dto, @AuthenticationPrincipal Usuario logado) {
        return ResponseEntity.ok(usuarioService.atualizarMe(dto, logado));
    }

    @Operation(summary = "Cadastra um novo usuário", description = "Endpoint público para registro de novos observadores ou administradores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(dto));
    }

    @Operation(summary = "Promove usuário a ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{email}/promover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> promover(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.atualizarRole(email, Role.ADMIN));
    }

    @Operation(summary = "Remove um usuário", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletar(@PathVariable String email, @AuthenticationPrincipal Usuario executor) {
        usuarioService.deletarConta(email, executor);
        return ResponseEntity.noContent().build();
    }
}