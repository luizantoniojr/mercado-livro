# Mercado Livro

API REST desenvolvida em Kotlin para gerenciamento de livros e clientes. Este projeto foi desenvolvido para praticar conceitos de:

- Kotlin
- Spring Boot
- API REST
- JPA/Hibernate
- MySQL

## Tecnologias Utilizadas

- Kotlin
- Spring Boot
- Spring Data JPA
- MySQL
- Gradle
- Flyway (Migrations)

## Funcionalidades

### Clientes (Customers)

- Listagem de clientes com filtro opcional por nome
- Cadastro de novo cliente
- Busca de cliente por ID
- Atualização de dados do cliente
- Remoção de cliente

## Estrutura do Projeto

```
src/main/kotlin/com/mercadolivro/
├── controller/          # Controladores REST
├── model/              # Entidades JPA
├── repository/         # Interfaces de repositório
├── service/           # Camada de serviços
└── extension/         # Funções de extensão Kotlin
```

## Como Executar

1. Clone o repositório
2. Configure o MySQL:
   - Database: app_db
   - Username: app_user
   - Password: app_pass
   - Porta: 3306

3. Execute o projeto usando Gradle:
   ```bash
   ./gradlew bootRun
   ```

## Endpoints

### Customers

```
GET /customers              # Lista todos os clientes
GET /customers?name=João    # Filtra clientes por nome
POST /customers            # Cria novo cliente
GET /customers/{id}        # Busca cliente por ID
PUT /customers/{id}        # Atualiza dados do cliente
DELETE /customers/{id}     # Remove cliente
```

### Exemplo de Payload (Cliente)

```json
{
    "name": "Nome do Cliente",
    "email": "cliente@email.com"
}
```

## Configuração do Banco de Dados

O projeto utiliza MySQL como banco de dados. As configurações de conexão podem ser encontradas em `application.properties`:

- URL: jdbc:mysql://localhost:3306/app_db
- Username: app_user
- Password: app_pass

## Migrations

O projeto utiliza Flyway para controle de migrations. Os scripts de migração estão localizados em:
`src/main/resources/db/migrations/` 