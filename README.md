# Monitoramento Meteorológico API

Esta é uma API RESTful desenvolvida para o **Trabalho I da disciplina de Serviços Web**, do curso de Ciência da Computação. A aplicação permite o gerenciamento de estações meteorológicas e o registro de leituras de sensores (temperatura, umidade, pressão, etc.).

---

## Descrição do Cenário
O sistema foi projetado para simular uma rede de monitoramento climático inspirada no modelo do INMET.
- **Usuários:** Podem ser observadores (USER) ou administradores (ADMIN).
- **Estações:** Identificadas por código único, vinculadas a um usuário responsável.
- **Leituras:** Registros históricos de sensores enviados periodicamente.

---

## Tecnologias e Pesquisa (Contextualização)
A escolha da stack tecnológica baseou-se nos padrões atuais de mercado e nos requisitos de escalabilidade e segurança:

* **Spring Boot 3 & Java 21:** Escolhido pela robustez e facilidade na criação de microserviços. A arquitetura foi organizada em camadas (**Controller, Service, Repository**) para garantir a separação de responsabilidades.
* **Spring Security & JWT (Stateless):** Implementamos autenticação via tokens JWT com criptografia RSA (chaves pública/privada). Isso permite que a API seja *Stateless*, não mantendo sessões no servidor, o que facilita o escalonamento.
* **PostgreSQL & JSONB:** Utilizamos o Postgres para dados relacionais. O diferencial técnico foi o uso do tipo **JSONB** na coluna de metadados das estações, permitindo que diferentes modelos de sensores enviem propriedades variadas sem a necessidade de alterar o esquema do banco de dados.
* **Flyway:** Migrações de banco de dados para garantir versionamento e consistência entre ambientes.

---

## Como Executar

### Pré-requisitos
* Java 21 (JDK)
* PostgreSQL rodando localmente
* Maven (ou use o wrapper `./mvnw` incluso)

### Instalação
1. Clone o repositório.
2. Configure o arquivo `src/main/resources/application.yaml` com as credenciais do seu banco de dados.
3. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acesse a documentação interativa em: `http://localhost:8080/swagger-ui/index.html`

---

## TABELAS DE ROTAS

### Autenticação

| Verbo | Rota          | Descrição                         | Status Sucesso |
|-------|--------------|----------------------------------|----------------|
| POST  | /auth/login  | Login e geração de Token JWT     | 200 OK         |
| POST  | /usuarios    | Cadastro de novo usuário         | 201 Created    |

---

### Usuários

| Verbo | Rota                          | Descrição                              | Acesso        |
|-------|------------------------------|----------------------------------------|--------------|
| GET   | /usuarios/me                 | Obtém dados do usuário logado          | USER / ADMIN |
| PUT   | /usuarios/me                 | Atualiza nome/senha próprios           | USER / ADMIN |
| PATCH | /usuarios/{email}/promover   | Eleva usuário a ADMIN                  | APENAS ADMIN |
| DELETE| /usuarios/{email}            | Remove conta (própria ou via ADMIN)    | USER / ADMIN |

---

### Estações

| Verbo | Rota                  | Descrição                              | Acesso        |
|-------|----------------------|----------------------------------------|--------------|
| GET   | /estacoes            | Lista todas as estações ativas         | Público      |
| GET   | /estacoes/{id}       | Busca detalhes de uma estação          | USER / ADMIN |
| POST  | /estacoes            | Cria uma nova estação                  | USER / ADMIN |
| PUT   | /estacoes/{id}       | Atualiza dados da estação              | DONO / ADMIN |
| DELETE| /estacoes/{id}       | Desativa estação (Soft Delete)         | DONO / ADMIN |

---

### Leituras

| Verbo | Rota                                | Descrição                              | Acesso        |
|-------|------------------------------------|----------------------------------------|--------------|
| POST  | /estacoes/{id}/leituras            | Registra dados de sensores             | DONO / ADMIN |
| GET   | /estacoes/{id}/leituras            | Lista histórico da estação             | Público      |
| GET   | /estacoes/{id}/leituras/recente    | Obtém o último dado enviado            | Público      |
| DELETE| /estacoes/{id}/leituras/{id}       | Remove um registro de leitura          | DONO / ADMIN |