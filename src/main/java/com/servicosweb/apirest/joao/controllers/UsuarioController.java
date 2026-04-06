package com.servicosweb.apirest.joao.controllers;

import com.servicosweb.apirest.joao.dtos.usuario.UsuarioResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PatchMapping("/{email}/promover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> promover(@PathVariable String email, @AuthenticationPrincipal Usuario executor) {
        return ResponseEntity.ok(usuarioService.atualizarRole(email, Role.ADMIN, executor));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletar(@PathVariable String email, @AuthenticationPrincipal Usuario executor) {
        usuarioService.deletarConta(email, executor);
        return ResponseEntity.noContent().build();
    }
}
