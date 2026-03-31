package com.servicosweb.apirest.joao.dtos.usuario;

import com.servicosweb.apirest.joao.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 6) String senha,
        Role role
) {}