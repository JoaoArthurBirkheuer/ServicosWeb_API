-- Não utilize estas credenciais em produção
-- Senha: admin123 (ADMIN) | user123 (USER)

INSERT INTO usuarios (nome, email, senha, role) VALUES
    ('Administrador INMET',
     'admin@meteorologia.example',
     '$2a$10$7QJ3Fk9VzEoXmQ1Kj3N0OeWQZ6hYkXdJwU5vL8pT2rM4sN6bC9yAi',
     'ADMIN'),

    ('João Pesquisador',
     'joao.pesquisador@meteorologia.example',
     '$2a$10$Kp2RzNqW8mVjT4sX1bD5OuFhL9eYcGnI6vM3tJ7rP0kQ5wA2xN8Cf',
     'USER'),

    ('Maria Analista',
     'maria.analista@meteorologia.example',
     '$2a$10$Kp2RzNqW8mVjT4sX1bD5OuFhL9eYcGnI6vM3tJ7rP0kQ5wA2xN8Cf',
     'USER');

INSERT INTO estacoes (nome, codigo, ativa, metadata, usuario_id) VALUES
    (
        'Estação Passo Fundo',
        'RS-PF-001',
        TRUE,
        '{
            "regiao": "Sul",
            "hemisferio": "Sul",
            "pais": "Brasil",
            "estado": "Rio Grande do Sul",
            "cidade": "Passo Fundo",
            "centro_pesquisa": "INMET",
            "latitude": -28.2631,
            "longitude": -52.4069,
            "altitude_m": 687
        }',
        1
    ),
    (
        'Estação Porto Alegre',
        'RS-POA-001',
        TRUE,
        '{
            "regiao": "Sul",
            "hemisferio": "Sul",
            "pais": "Brasil",
            "estado": "Rio Grande do Sul",
            "cidade": "Porto Alegre",
            "centro_pesquisa": "INMET",
            "latitude": -30.0346,
            "longitude": -51.2177,
            "altitude_m": 10
        }',
        1
    ),
    (
        'Estação Manaus',
        'AM-MAN-001',
        TRUE,
        '{
            "regiao": "Norte",
            "hemisferio": "Sul",
            "pais": "Brasil",
            "estado": "Amazonas",
            "cidade": "Manaus",
            "centro_pesquisa": "SIPAM",
            "latitude": -3.1019,
            "longitude": -60.0250,
            "altitude_m": 72
        }',
        1
    ),
    (
        'Estação Brasília',
        'DF-BSB-001',
        TRUE,
        '{
            "regiao": "Centro-Oeste",
            "hemisferio": "Sul",
            "pais": "Brasil",
            "estado": "Distrito Federal",
            "cidade": "Brasília",
            "centro_pesquisa": "INMET",
            "latitude": -15.7801,
            "longitude": -47.9292,
            "altitude_m": 1172
        }',
        1
    ),
    (
        'Estação Recife',
        'PE-REC-001',
        FALSE,
        '{
            "regiao": "Nordeste",
            "hemisferio": "Sul",
            "pais": "Brasil",
            "estado": "Pernambuco",
            "cidade": "Recife",
            "centro_pesquisa": "APAC",
            "latitude": -8.0539,
            "longitude": -34.8811,
            "altitude_m": 4
        }',
        1
    );

INSERT INTO leituras (estacao_id, timestamp_leitura, temperatura, umidade, pressao, velocidade_vento, direcao_vento, precipitacao, qualidade) VALUES
    -- Passo Fundo
    (1, NOW() - INTERVAL '24 hours', 14.2, 82.0, 933.5, 12.0, 180.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '23 hours', 13.8, 84.0, 933.2, 10.5, 175.0, 0.2,  'OK'),
    (1, NOW() - INTERVAL '22 hours', 13.1, 87.0, 932.8, 8.0,  170.0, 1.4,  'OK'),
    (1, NOW() - INTERVAL '21 hours', 12.5, 90.0, 932.4, 6.5,  165.0, 3.2,  'OK'),
    (1, NOW() - INTERVAL '20 hours', 12.0, 92.0, 932.0, 5.0,  160.0, 5.8,  'OK'),
    (1, NOW() - INTERVAL '19 hours', 11.8, 93.0, 931.8, 4.5,  155.0, 4.1,  'OK'),
    (1, NOW() - INTERVAL '18 hours', 11.5, 95.0, 931.5, 3.0,  150.0, 2.0,  'OK'),
    (1, NOW() - INTERVAL '17 hours', 12.2, 93.0, 931.8, 5.5,  160.0, 0.5,  'OK'),
    (1, NOW() - INTERVAL '16 hours', 13.0, 90.0, 932.2, 8.0,  170.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '15 hours', 14.5, 85.0, 932.8, 10.0, 180.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '14 hours', 16.2, 78.0, 933.4, 14.0, 190.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '13 hours', 18.0, 72.0, 934.0, 18.0, 200.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '12 hours', 19.8, 65.0, 934.5, 20.0, 210.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '11 hours', 21.3, 60.0, 934.8, 22.0, 215.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '10 hours', 22.5, 55.0, 935.0, 24.0, 220.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '9 hours',  23.1, 53.0, 935.1, 25.0, 225.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '8 hours',  23.4, 52.0, 935.0, 26.0, 230.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '7 hours',  22.8, 54.0, 934.8, 23.0, 225.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '6 hours',  21.5, 58.0, 934.5, 20.0, 215.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '5 hours',  20.0, 63.0, 934.2, 17.0, 205.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '4 hours',  18.5, 68.0, 934.0, 14.0, 195.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '3 hours',  17.2, 73.0, 933.8, 11.0, 185.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '2 hours',  16.0, 78.0, 933.6, 9.0,  180.0, 0.0,  'OK'),
    (1, NOW() - INTERVAL '1 hour',   15.1, 80.0, 933.5, 8.5,  178.0, 0.0,  'OK'),

    -- Passo Fundo
    (1, NOW() - INTERVAL '30 hours', 99.9,  10.0, 800.0, 0.0, 0.0, 0.0,  'INVALIDO'),
    (1, NOW() - INTERVAL '48 hours', 25.0,  NULL, 934.0, 15.0, 200.0, 0.0, 'SUSPEITO'),

    -- Porto Alegre
    (2, NOW() - INTERVAL '6 hours',  22.0, 75.0, 1012.5, 18.0, 90.0,  0.0,  'OK'),
    (2, NOW() - INTERVAL '5 hours',  22.5, 74.0, 1012.8, 20.0, 95.0,  0.0,  'OK'),
    (2, NOW() - INTERVAL '4 hours',  23.1, 72.0, 1013.0, 22.0, 100.0, 0.0,  'OK'),
    (2, NOW() - INTERVAL '3 hours',  23.8, 70.0, 1013.2, 19.0, 98.0,  0.0,  'OK'),
    (2, NOW() - INTERVAL '2 hours',  24.0, 69.0, 1013.1, 17.0, 95.0,  0.0,  'OK'),
    (2, NOW() - INTERVAL '1 hour',   23.5, 71.0, 1013.0, 15.0, 90.0,  0.0,  'OK'),

    -- Manaus
    (3, NOW() - INTERVAL '6 hours',  31.5, 88.0, 1009.2, 8.0,  45.0,  12.5, 'OK'),
    (3, NOW() - INTERVAL '5 hours',  32.0, 86.0, 1009.0, 9.5,  50.0,  8.0,  'OK'),
    (3, NOW() - INTERVAL '4 hours',  33.2, 82.0, 1008.8, 11.0, 55.0,  2.0,  'OK'),
    (3, NOW() - INTERVAL '3 hours',  33.8, 80.0, 1008.5, 12.0, 60.0,  0.0,  'OK'),
    (3, NOW() - INTERVAL '2 hours',  32.5, 83.0, 1008.7, 10.0, 52.0,  5.5,  'OK'),
    (3, NOW() - INTERVAL '1 hour',   31.0, 87.0, 1009.0, 7.5,  45.0,  18.0, 'OK'),

    -- Brasília
    (4, NOW() - INTERVAL '4 hours',  28.0, 45.0, 885.2, 15.0, 135.0, 0.0,  'OK'),
    (4, NOW() - INTERVAL '3 hours',  29.5, 42.0, 885.0, 17.0, 140.0, 0.0,  'OK'),
    (4, NOW() - INTERVAL '2 hours',  30.2, 40.0, 884.8, 20.0, 145.0, 0.0,  'OK'),
    (4, NOW() - INTERVAL '1 hour',   29.8, 41.0, 885.0, 18.0, 142.0, 0.0,  'SUSPEITO');
