package com.servicosweb.apirest.joao.dtos.estacao;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record EstacaoResponseDTO(
        String nome,
        String codigo,
        Boolean ativa,
        JsonNode metadata,
        String emailProprietario,
        LocalDateTime createdAt
) {}