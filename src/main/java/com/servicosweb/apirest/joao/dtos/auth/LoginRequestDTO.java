package com.servicosweb.apirest.joao.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String email,
        @NotBlank String senha
) {}
