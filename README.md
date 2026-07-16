# Paciente API - EBSERH

API REST para gerenciamento de pacientes hospitalares, com operações de CRUD, regras de negócio para internação e documentação via OpenAPI/Swagger.

## Arquitetura

O projeto segue uma organização em camadas (arquitetura em camadas / layered architecture), típica de aplicações Spring Boot:

```
controller   → recebe requisições HTTP, delega para o service
service      → regras de negócio e orquestração
repository   → acesso a dados (Spring Data JPA)
mapper       → conversão entre Entity e DTOs
dto          → contratos de entrada (request) e saída (response), separados
entity       → mapeamento JPA (Paciente, EntidadeBase)
enums        → domínios fechados (TipoSanguineo, AmbienteInternacao)
exception    → exceções de domínio + tratamento global
config       → configuração de infraestrutura (OpenAPI)
```

**Fluxo de uma requisição:** `Controller` → valida o DTO (Bean Validation) → chama o `Service` → aplica regras de negócio → usa `Mapper` para converter DTO/Entity → persiste via `Repository` → retorna `DTO de resposta`.

## Abordagem e decisões de design

- **Separação Request/Response**: `CriarPacienteRequest` e `AtualizarPacienteRequest` herdam de `PacienteRequest` (classe abstrata com as validações comuns), evitando duplicação enquanto mantêm contratos de entrada independentes do contrato de saída (`PacienteResponse`).
- **Entidade base auditável**: `EntidadeBase` centraliza `id` (UUID gerado pela aplicação), `criadoEm` e `atualizadoEm`, preenchidos automaticamente via `@PrePersist`/`@PreUpdate`. `Paciente` herda dela (`@MappedSuperclass`).
- **Mapper manual**: `PacienteMapper` converte Entity ↔ DTO sem biblioteca externa (ex: MapStruct), priorizando explicitude sobre "mágica" de geração de código.
- **Regra de negócio isolada no Service**: a validação de internação (paciente internado exige ambiente e leito; não internado não pode ter nenhum dos dois) fica centralizada em `validarInternacao`, reaproveitada tanto na criação quanto na atualização.
- **Exceções de domínio + handler global**: `CpfJaCadastradoException`, `PacienteNaoEncontradoException` e `RegraNegocioException` são traduzidas para respostas HTTP consistentes (404, 409, 400) pelo `GlobalExceptionHandler`, com um formato de erro padronizado (`ErroResponse`).
- **Migrations versionadas com Flyway**: o schema (tabela `pacientes`, índice único em `cpf`) é criado via `V1__create_patient_table.sql`, com `ddl-auto: validate` no Hibernate — ou seja, o Hibernate nunca altera o schema, apenas valida que a entidade bate com o banco.
- **CPF único**: garantido em dois níveis — constraint de banco (`idx_paciente_cpf` unique) e checagem prévia na aplicação (`existsByCpf` / `existsByCpfAndIdNot`), essa última para retornar um erro de negócio (409) em vez de deixar estourar uma exceção de constraint do banco.

## Boas práticas aplicadas

- Bean Validation (`@NotBlank`, `@NotNull`, `@Past`, `@Positive`, `@CPF`) nos DTOs de entrada, com mensagens de erro em português.
- `open-in-view: false`, evitando o anti-padrão de manter a sessão do Hibernate aberta durante a renderização da resposta.
- Uso de `ResponseEntity` com status HTTP semânticos (`201 Created`, `204 No Content`, `404`, `409`, `400`).
- Testes em três camadas, cada uma isolando sua responsabilidade (ver seção de testes).
- Lombok para reduzir boilerplate (`@Getter`/`@Setter`/`@Builder`/`@RequiredArgsConstructor`), mantendo as classes de domínio enxutas.
- `.gitignore` cobrindo `target/`, IDEs e artefatos de build.

## Tecnologias

| Categoria       | Tecnologia                                  |
|-----------------|----------------------------------------------|
| Linguagem       | Java 21                                       |
| Framework       | Spring Boot 3.5.5 (Web, Validation, Data JPA) |
| Banco de dados  | PostgreSQL                                    |
| Migrations      | Flyway                                        |
| Documentação    | springdoc-openapi (Swagger UI)                |
| Testes          | JUnit 5, Mockito, MockMvc, H2 (banco em memória) |
| Build           | Maven                                         |
| Containerização | Docker / Docker Compose                       |

## Como rodar o projeto

Pré-requisitos: Docker e Docker Compose instalados.

```bash
docker compose up -d --build
```

Isso sobe o PostgreSQL (porta `5432`) e a API (porta `8080`), já aplicando as migrations do Flyway na inicialização.

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Como rodar os testes

```bash
./mvnw test
```

Os testes **não dependem do PostgreSQL** — usam H2 em memória, então rodam isolados do ambiente Docker:

- **`PacienteControllerTest`** (`@WebMvcTest`): testa a camada web isoladamente, mockando o `PacienteService` (`@MockitoBean`) e validando contrato HTTP via `MockMvc`.
- **`PacienteServiceTest`** (`MockitoExtension`): testa as regras de negócio isolando `PacienteRepository` e `PacienteMapper` com mocks.
- **`PacienteRepositoryTest`** (`@DataJpaTest`): testa as queries reais contra um banco H2 em memória, validando persistência e os métodos customizados (`existsByCpf`, `findByCpf`).
- **`PacienteApiEbserhApplicationTests`**: smoke test de contexto (`contextLoads`).

## Endpoints

| Método | Rota              | Descrição                    |
|--------|-------------------|-------------------------------|
| POST   | `/pacientes`      | Cria um paciente               |
| GET    | `/pacientes`      | Lista todos os pacientes       |
| GET    | `/pacientes/{id}` | Busca paciente por ID           |
| PUT    | `/pacientes/{id}` | Atualiza um paciente            |
| DELETE | `/pacientes/{id}` | Remove um paciente              |
