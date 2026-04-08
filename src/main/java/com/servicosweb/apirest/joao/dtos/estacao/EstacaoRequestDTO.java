package com.servicosweb.apirest.joao.dtos.estacao;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstacaoRequestDTO(
        @NotBlank(message = "Nome de estação é obrigatório") @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,
        @NotBlank(message = "Código é obrigatório") @Size(max = 20, message = "Código deve ter no máximo 20 caracteres")  String codigo,
        JsonNode metadata
) {}
