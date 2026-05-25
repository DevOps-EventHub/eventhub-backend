# eventhub-backend
# EventHub Backend

## Descricao do projeto
API REST do EventHub, responsável pelas regras de negócio, autenticação e persistência de dados de eventos.

## Objetivo da aplicacao
Oferecer endpoints para cadastro, listagem, edição e remoção de eventos, integrados ao banco PostgreSQL.

## Tecnologias utilizadas
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway
- PostgreSQL
- Maven

## Instruções para execução
1. Garanta um PostgreSQL ativo.
2. Configure variáveis de ambiente (ou `.env`) conforme exemplo.
3. Execute a aplicação com Maven Wrapper.

## Comandos principais
```bash
./mvnw spring-boot:run
./mvnw test
./mvnw clean package
```

## Estrutura básica do projeto
- `src/main/java/com/eventhub/controller`: endpoints REST.
- `src/main/java/com/eventhub/service`: regras de negócio.
- `src/main/java/com/eventhub/repository`: acesso a dados.
- `src/main/resources/db/migration`: migrações Flyway.
- `src/test`: testes automatizados.

Documentação integrada (infra, frontend e backend):
- https://github.com/DevOps-EventHub/eventhub-infra

## Integrantes da equipe
- Samuel Araujo
- José Pereira Neto
- José Mailson
