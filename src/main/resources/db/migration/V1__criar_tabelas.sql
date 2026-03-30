
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuarios (
    id          BIGSERIAL       PRIMARY KEY,
    nome        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    senha       VARCHAR(255)    NOT NULL,
    role        VARCHAR(20)     NOT NULL DEFAULT 'USER'
                                CHECK (role IN ('ADMIN', 'USER')),
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE TABLE estacoes (
    id          BIGSERIAL       PRIMARY KEY,
    nome        VARCHAR(150)    NOT NULL,
    codigo      VARCHAR(20)     NOT NULL UNIQUE,
    ativa       BOOLEAN         NOT NULL DEFAULT TRUE,
    metadata    JSONB,
    -- metadata:
    -- {
    --   "regiao": "Sul",
    --   "hemisferio": "Sul",
    --   "pais": "Brasil",
    --   "centro_pesquisa": "INMET",
    --   "latitude": -28.26,
    --   "longitude": -52.40
    -- }
    usuario_id  BIGINT          NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_estacoes_usuario ON estacoes(usuario_id);
CREATE INDEX idx_estacoes_codigo  ON estacoes(codigo);
CREATE INDEX idx_estacoes_ativa   ON estacoes(ativa);
CREATE INDEX idx_estacoes_metadata ON estacoes USING GIN (metadata);

CREATE TABLE leituras (
    id                  BIGSERIAL       PRIMARY KEY,
    estacao_id          BIGINT          NOT NULL REFERENCES estacoes(id) ON DELETE CASCADE,
    timestamp_leitura   TIMESTAMP       NOT NULL DEFAULT NOW(),
    temperatura         NUMERIC(5, 2),              -- °C
    umidade             NUMERIC(5, 2),              -- %
    pressao             NUMERIC(7, 2),              -- hPa
    velocidade_vento    NUMERIC(6, 2),              -- km/h
    direcao_vento       NUMERIC(5, 1),              -- graus (0–360)
    precipitacao        NUMERIC(6, 2),              -- mm
    qualidade           VARCHAR(20)     NOT NULL DEFAULT 'OK'
                                        CHECK (qualidade IN ('OK', 'SUSPEITO', 'INVALIDO')),
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_leituras_estacao   ON leituras(estacao_id);
CREATE INDEX idx_leituras_timestamp ON leituras(timestamp_leitura DESC);
CREATE INDEX idx_leituras_qualidade ON leituras(qualidade);
