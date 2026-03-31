package com.servicosweb.apirest.joao.dtos.leitura;

import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeituraResponseDTO(
        LocalDateTime timestampLeitura,
        BigDecimal temperatura,
        BigDecimal umidade,
        BigDecimal pressao,
        BigDecimal velocidadeVento,
        BigDecimal direcaoVento,
        BigDecimal precipitacao,
        QualidadeLeitura qualidade
) {}