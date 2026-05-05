# Garage System — Sitema de Gestão de Oficina

Sistema back-end para gestão de uma oficina mecânica, desenvolvido como MVP para o **Tech Challenge — Fase 1** da Pós-Graduação POSTECH em Software Architecture.

---

## Índice

- [Sobre o projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Como executar](#como-executar)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Documentação da API](#documentação-da-api)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)

---

## Sobre o projeto

O **Garage System** é um sistema integrado que permite à oficina mecânica gerenciar todo o ciclo de atendimento de veículos, desde a abertura da Ordem de Serviço até a entrega ao cliente.

O cliente pode acompanhar em tempo real o status do serviço e autorizar reparos via e-mail, enquanto a equipe interna gerencia ordens de serviço, peças, insumos e serviços de forma eficiente e segura.

---

## Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework back-end |
| Spring Security | incluso no Spring Boot | Autenticação e autorização |
| PostgreSQL | 16 | Banco de dados principal |
| H2 | - | Banco em memória para testes |
| Flyway | - | Migrations do banco de dados |
| JWT (jjwt) | 0.12.5 | Autenticação stateless |
| springdoc-openapi | 2.x | Documentação Swagger |
| Lombok | - | Redução de boilerplate |
| JaCoCo | 0.8.11 | Cobertura de testes |
| Docker | - | Containerização |
| MailHog | - | Servidor SMTP para desenvolvimento |

### Por que PostgreSQL?

- **ACID completo** — garante consistência nas transações críticas (ex: decrementar estoque e criar OS atomicamente)
- **Suporte nativo a JPA/Hibernate** — sem configurações extras
- **Gratuito e open-source** — sem custo de licença
- **Imagem Docker leve** — `postgres:16-alpine`
- **Amplamente usado em produção** — maturidade e documentação abundante

---

## Arquitetura

O projeto segue **Domain-Driven Design (DDD)** com arquitetura em camadas:

```
src/main/java/com/pedrocmoreira/garagesystem/
│
├── domain/              ← Regras de negócio puras. Zero dependências externas.
│   ├── model/           ← Entidades, Agregados, Enums de estado
│   ├── repository/      ← Interfaces (portas de saída)
│   ├── service/         ← Domain Services e Validadores
│   └── exception/       ← Exceções do domínio
│
├── application/         ← Casos de uso. Orquestra domain + repositories.
│   └── usecase/
│
├── infrastructure/      ← Detalhes técnicos: JPA, JWT, Spring Security, e-mail
│   ├── persistence/
│   ├── security/
│   ├── mail/
│   └── config/
│
└── presentation/        ← Controllers REST, DTOs, tratamento de erros
    └── controller/
```

---

## Funcionalidades

### Fluxo da Ordem de Serviço

```
RECEBIDA → EM_DIAGNOSTICO → AGUARDANDO_APROVACAO → EM_EXECUCAO → FINALIZADA → ENTREGUE
                                      ↓
                                  CANCELADA
```

Ao atingir `AGUARDANDO_APROVACAO`, o sistema envia automaticamente um e-mail ao cliente com links para **aprovar** ou **recusar** o orçamento.

### APIs disponíveis

| Recurso | Método + Rota | Auth |
|---|---|---|
| Login | `POST /api/auth/login` | Público |
| Consulta de OS pelo cliente | `GET /api/consult/service-order/{number}` | Público |
| Aprovar orçamento (via e-mail) | `GET /api/budget/{number}/approve` | Público |
| Recusar orçamento (via e-mail) | `GET /api/budget/{number}/refuse` | Público |
| Clientes | `GET/POST/PUT/DELETE /api/customers` | JWT |
| Veículos | `GET/POST/PUT/DELETE /api/vehicles` | JWT |
| Serviços | `GET/POST/PUT/DELETE /api/services` | JWT |
| Peças e Insumos | `GET/POST/PUT/PATCH/DELETE /api/parts` | JWT |
| Ordens de Serviço | `GET/POST/PATCH /api/service-orders` | JWT |
| Tempo médio de execução | `GET /api/service-orders/report/average-time` | JWT |

### Funcionalidades administrativas

- CRUD completo de clientes (CPF/CNPJ validado matematicamente)
- CRUD completo de veículos (placa validada — formato antigo e Mercosul)
- CRUD completo de peças e insumos com controle de estoque
- CRUD completo de serviços
- Listagem e detalhamento de ordens de serviço (por ID, status ou listagem geral)
- Monitoramento do tempo médio de execução dos serviços
- Vinculação de peças adicionais a uma OS em andamento

---

## Como executar

### Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados
- Java 17 e Maven (opcional — apenas para desenvolvimento local sem Docker)

### Com Docker (recomendado)

O `docker-compose` sobe três serviços: a aplicação, o PostgreSQL e o MailHog (servidor de e-mail para desenvolvimento).

```bash
# 1. Clone o repositório
git clone https://github.com/pedrocmoreira/tech-challenge-fase-01.git
cd tech-challenge-fase-01

# 2. Crie o arquivo de variáveis de ambiente
cp .env.example .env
# Edite o .env com suas credenciais (veja a seção Variáveis de ambiente)

# 3. Suba o ambiente completo
docker-compose up --build

# 4. Acesse
# API:     http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# MailHog: http://localhost:8025
```

### Apenas o banco (para desenvolvimento local com IDE)

```bash
# Sobe apenas o PostgreSQL e o MailHog
docker-compose up db mailhog

# Execute a aplicação pela IDE ou pelo Maven Wrapper
./mvnw spring-boot:run
```

### Derrubar o ambiente

```bash
# Para os containers
docker-compose down

# Para os containers e remove os volumes (apaga o banco)
docker-compose down -v
```

---

## Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis (usadas pelo `docker-compose`):

| Variável | Exemplo | Descrição |
|---|---|---|
| `DB_HOST` | `db` | Host do PostgreSQL |
| `DB_PORT` | `5432` | Porta do PostgreSQL |
| `DB_NAME` | `garagesystem_db` | Nome do banco |
| `DB_USER` | `garage` | Usuário do banco |
| `DB_PASS` | `garage123` | Senha do banco |
| `JWT_SECRET` | `sua-chave-secreta` | Chave de assinatura JWT — **obrigatório trocar em produção** |
| `ADMIN_USERNAME` | `admin` | Usuário administrador criado no primeiro boot |
| `ADMIN_PASSWORD` | `admin123` | Senha do administrador — **trocar em produção** |
| `MAIL_HOST` | `mailhog` | Host SMTP |
| `MAIL_PORT` | `1025` | Porta SMTP |
| `MAIL_USERNAME` | *(vazio)* | Usuário SMTP (não exigido pelo MailHog) |
| `MAIL_PASSWORD` | *(vazio)* | Senha SMTP (não exigida pelo MailHog) |
| `MAIL_FROM` | `noreply@garagesystem.com` | Endereço remetente dos e-mails |
| `APP_BASE_URL` | `http://localhost:8080` | URL base usada nos links enviados por e-mail |

---

## Documentação da API

Com a aplicação rodando, acesse o Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

### Autenticação no Swagger

1. Acesse `POST /api/auth/login` com as credenciais do administrador
2. Copie o `token` retornado
3. Clique no botão **Authorize** no topo da página
4. Digite `Bearer {seu_token}` e clique em **Authorize**

---

## Testes

### Executar todos os testes

```bash
./mvnw test
```

### Executar com relatório de cobertura

```bash
./mvnw verify
```

O relatório de cobertura (JaCoCo) é gerado em:
```
target/site/jacoco/index.html
```

### Estrutura dos testes

| Arquivo | Tipo | Camada |
|---|---|---|
| `StatusSOTest` | Unitário | domain/model |
| `PartTest` | Unitário | domain/model |
| `ServiceOrderTest` | Unitário | domain/model |
| `ValidatorTest` | Unitário | domain/service |
| `CreateServiceOrderUseCaseTest` | Unitário (Mockito) | application/usecase |
| `BudgetApproveUseCaseTest` | Unitário (Mockito) | application/usecase |
| `StatusAndLinkUseCaseTest` | Unitário (Mockito) | application/usecase |
| `GarageSystemApplicationTests` | Integração | contexto Spring |

---

## Estrutura do projeto

```
garage-system/
├── src/
│   ├── main/
│   │   ├── java/com/pedrocmoreira/garagesystem/
│   │   │   ├── domain/
│   │   │   ├── application/
│   │   │   ├── infrastructure/
│   │   │   └── presentation/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── templates/email/
│   │       └── db/migration/
│   │           ├── V1__create_customers.sql
│   │           ├── V2__create_vehicles.sql
│   │           ├── V3__create_services_parts.sql
│   │           ├── V4__create_service_orders.sql
│   │           ├── V5__create_users.sql
│   │           └── V6__add_diagnosis_start_date_to_service_orders.sql
│   └── test/
│       ├── java/com/pedrocmoreira/garagesystem/
│       └── resources/
│           └── application-test.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Segurança

- Autenticação via **JWT** em todas as rotas administrativas
- Rotas públicas (`/api/consult/**`, `/api/budget/**`, `/api/auth/**`, Swagger) liberadas sem autenticação
- Senhas armazenadas com **BCrypt**
- Validação matemática de **CPF e CNPJ** (dígitos verificadores)
- Validação de **placa veicular** nos formatos antigo (ABC1234) e Mercosul (ABC1D23)
- Análise de vulnerabilidades realizada com **OWASP Dependency Check**

---

## Documentação DDD

A documentação completa do Domain-Driven Design, incluindo Event Storming, diagramas de domínio e Linguagem Ubíqua, está disponível no Miro:

[Link da documentação no Miro](#) ← substituir pelo link real

---

*Desenvolvido para o Tech Challenge — Fase 1 | POSTECH 2025*
