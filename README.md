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

4. Acesse a documentação Swagger UI:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Endpoints

### Customers

```