package com.servicosweb.apirest.joao.dtos.estacao;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstacaoRequestDTO(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Size(max = 20)  String codigo,
        JsonNode metadata
) {}
