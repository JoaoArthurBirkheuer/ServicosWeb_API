package com.servicosweb.apirest.joao.dtos.usuario;

import com.servicosweb.apirest.joao.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "Nome não pode ser nulo") String nome,
        @NotBlank(message = "E-mail não pode ser nulo") @Email(message = "E-mail não tem formato adequado") String email,
        @NotBlank(message="Senha não pode ser nula") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String senha,
        Role role,
        String adminKey
) {}