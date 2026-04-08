package com.servicosweb.apirest.joao.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "E-mail não pode ser nulo") @Email(message = "E-mail não tem formato adequado")
        String email,
        @NotBlank(message = "Senha não pode ser nula") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String senha
) {}
