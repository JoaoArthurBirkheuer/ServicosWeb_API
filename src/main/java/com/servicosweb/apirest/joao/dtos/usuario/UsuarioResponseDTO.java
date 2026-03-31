package com.servicosweb.apirest.joao.dtos.usuario;

import com.servicosweb.apirest.joao.enums.Role;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        String nome,
        String email,
        Role role,
        LocalDateTime createdAt
) {}