package com.servicosweb.apirest.joao.entities;

import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "leituras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacao_id", nullable = false)
    private Estacao estacao;

    @Column(name = "timestamp_leitura", nullable = false)
    private LocalDateTime timestampLeitura;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperatura;       // °C

    @Column(precision = 5, scale = 2)
    private BigDecimal umidade;           // %

    @Column(precision = 7, scale = 2)
    private BigDecimal pressao;           // hPa

    @Column(name = "velocidade_vento", precision = 6, scale = 2)
    private BigDecimal velocidadeVento;   // km/h

    @Column(name = "direcao_vento", precision = 5, scale = 1)
    private BigDecimal direcaoVento;      // graus (0–360)

    @Column(precision = 6, scale = 2)
    private BigDecimal precipitacao;      // mm

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QualidadeLeitura qualidade;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.timestampLeitura == null) this.timestampLeitura = LocalDateTime.now();
        if (this.qualidade == null) this.qualidade = QualidadeLeitura.OK;
    }
}