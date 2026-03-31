package com.servicosweb.apirest.joao.dtos.leitura;

import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeituraRequestDTO(
        @NotNull Long estacaoId,
        LocalDateTime timestampLeitura,
        BigDecimal temperatura,
        BigDecimal umidade,
        BigDecimal pressao,
        BigDecimal velocidadeVento,
        BigDecimal direcaoVento,
        BigDecimal precipitacao,
        QualidadeLeitura qualidade
) {}
