# Content Management & Social Interactions API

Esta é uma simulação de um backend desenvolvido para a gestão centralizada de perfis de usuários, publicações e fluxos de interações sociais (comentários). O projeto foi concebido sob os pilares da **Arquitetura Hexagonal (Ports and Adapters)**, garantindo total independência entre as regras de negócio e as tecnologias de infraestrutura.

---

## 🏗️ Arquitetura e Design de Software

O sistema utiliza o padrão **Hexagonal Architecture** para isolar o domínio da aplicação de detalhes técnicos externos.

### Estrutura de Pacotes e Camadas
```text
src/main/java/com/example/crudposts/
├── application/
│   ├── config/             # Beans de configuração (Swagger, JPA Auditing, Jackson)
│   └── controllers/        # Adaptadores de Entrada (REST API Endpoints)
├── domain/                 # Core da aplicação (Independente de Frameworks)
│   ├── entities/           # Mapeamento Objeto-Relacional (JPA/Hibernate)
│   ├── mappers/            # Conversores (Domain Models <-> Entities)
│   ├── models/             # Modelos de Domínio (Java Records para Imutabilidade)
│   └── services/           # Regras de Negócio e Orquestração de Fluxos
├── exceptions/             # Camada Transversal de Tratamento de Erros
│   ├── custom/             # Exceções de Domínio (BusinessException, EntityNotFound)
│   ├── handler/            # Global Exception Handler (@ControllerAdvice)
│   └── models/             # Estrutura de Resposta de Erro Padronizada (RFC 7807)
└── infra/                  # Infraestrutura e Persistência
    └── repository/         # Adaptadores de Saída (Spring Data Interfaces)
```

## Stack Tecnológica

* **Linguagem:** Java 25 (LTS)
* **Framework:** Spring Boot 3.4.x
* **Gerenciador de Build:** Gradle 8.11
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL 15
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Containerização:** Docker & Docker Compose
* **Auditoria:** Spring Data Auditing (Geração automática de timestamps)
* **Mapeamento:** MapStruct / Conversores Manuais (Isolamento de camadas)

---

## Guia de Execução (Ambiente Dockerizado)

A aplicação utiliza um **Dockerfile Multi-stage** para otimizar o tempo de build e reduzir a superfície de ataque da imagem final, separando o ambiente de compilação do ambiente de runtime.

### Passo a Passo para Inicialização:

1.  **Pré-requisitos:** Possuir Docker e Docker Compose instalados.
2.  **Subir o Ambiente:**
    ```bash
    docker-compose up --build -d
    ```
    *Este comando compila o código via Gradle dentro do container, configura o banco de dados PostgreSQL e inicia a API.*
3.  **Monitorar Inicialização:**
    ```bash
    docker logs -f crud-posts-app
    ```
4.  **URLs de Acesso:**
    * **API Base:** `http://localhost:8080`
    * **Documentação Interativa (Swagger):** `http://localhost:8080/swagger-ui/index.html`
  

## Documentação Detalhada da API (16 Endpoints)

### Usuários (`/users`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/users` | Registra um novo usuário no sistema. |
| **GET** | `/users/{id}` | Busca os detalhes de um perfil específico. |
| **PUT** | `/users/{id}` | Atualiza integralmente os dados do usuário. |
| **DELETE** | `/users/{id}` | Remove um usuário e todos os seus registros vinculados. |
| **GET** | `/users/{id}/posts` | Lista todas as publicações **públicas** de um usuário. |
| **GET** | `/users/{id}/comments` | Lista comentários realizados pelo usuário em posts públicos. |

### Publicações (`/posts`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/posts` | Cria uma nova publicação para um usuário. |
| **GET** | `/posts/{id}` | Retorna os detalhes de uma publicação específica. |
| **PUT** | `/posts/{id}` | Atualiza o conteúdo ou dados de uma publicação. |
| **DELETE** | `/posts/{id}` | Remove uma publicação permanentemente. |
| **PATCH** | `/posts/{id}/archive` | Altera o status da publicação para **arquivado**. |
| **PATCH** | `/posts/{id}/unarchive` | Altera o status da publicação para **ativo**. |
| **GET** | `/posts/{id}/comments` | Recupera todos os comentários de uma publicação. |

### Comentários (`/comments`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/comments` | Adiciona um comentário (bloqueado em posts arquivados). |
| **PUT** | `/comments/{id}` | Edita o conteúdo de um comentário existente. |
| **DELETE** | `/comments/{id}` | Remove um comentário permanentemente. |

---

##  Regras de Negócio e Lógica de Domínio

1.  **Estado de Publicação:** O sistema impede a criação de comentários em posts que possuem o status `archived: true`. Esta validação é feita na camada de serviço antes da persistência.
2.  **Visibilidade de Conteúdo:** Endpoints específicos filtram apenas publicações ativas para garantir a privacidade de conteúdos arquivados.
3.  **Isolamento de Camadas:** O uso de **Java Records** (Models) e Mappers garante que a entidade de banco de dados nunca seja exposta diretamente na API, seguindo os princípios da Arquitetura Hexagonal.

---

##  Padronização de Erros (RFC 7807)

A API utiliza um `GlobalExceptionHandler` para retornar erros no formato padrão da indústria:

```json
{
  "timestamp": "2026-04-04T22:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso com ID X não encontrado",
  "path": "/users/X"
}
```
* **404 (Not Found):** Retornado quando IDs de Usuários, Posts ou Comentários não são localizados no banco de dados.
* **400 (Bad Request):** Indica violação de regras de negócio (ex: tentativa de comentar em um post arquivado) ou falha na validação de campos obrigatórios.
* **500 (Internal Server Error):** Indica falhas inesperadas no servidor ou indisponibilidade temporária de serviços de infraestrutura (como o banco de dados).

## Configurações para Desenvolvimento Local

Caso opte por executar a aplicação fora do ambiente Docker (via IDE), utilize as seguintes credenciais padrão configuradas no `application.yml`:

* **URL:** `jdbc:postgresql://localhost:5432/crud_posts`
* **Username:** `user_starti`
* **Password:** `password_starti`

### Comandos Gradle Úteis:
* `./gradlew bootRun`: Inicia o servidor de desenvolvimento localmente.
* `./gradlew test`: Executa a suíte completa de testes unitários e de integração.
* `./gradlew bootJar`: Compila e gera o arquivo executável otimizado (`.jar`) no diretório `build/libs/`.

##  Exemplos de Testes (cURL)

Para testar a API via terminal, utilize os comandos abaixo. Lembre-se de substituir `{id}` pelo UUID real gerado pelo sistema.

###  Usuários (`/users`)

**Criar Usuário:**
```bash
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username": "zematheus", "name": "José Matheus", "email": "jose@teste.com", "password": "123", "biography": "Backend Engineer"}'
```
**Buscar por ID:**
```bash
curl -X GET http://localhost:8080/users/{id}
```
**Atualizar Usuário:**
```bash
curl -X PUT http://localhost:8080/users/{id} -H "Content-Type: application/json" -d '{"username": "zematheus_dev", "name": "José M. Atualizado", "email": "jose_novo@teste.com", "password": "456", "biography": "Java 25 Dev"}'
```
**Deletar Usuário:**
```bash
curl -X DELETE http://localhost:8080/users/{id}
```
**Listar Posts Públicos do Usuário:**
```bash
curl -X GET http://localhost:8080/users/{id}/posts
```
**Listar Comentários do Usuário em Posts Públicos:**
```bash
curl -X GET http://localhost:8080/users/{id}/comments
```

###  Posts (`/posts`)

**Criar Publicação:**

```bash
curl -X POST http://localhost:8080/posts -H "Content-Type: application/json" -d '{"userId": "{user_id}", "text": "Olá mundo!", "archived": false}'
```
**Buscar Publicação por ID:**

```bash
curl -X GET http://localhost:8080/posts/{id}
```
**Atualizar Publicação:**

```bash
curl -X PUT http://localhost:8080/posts/{id} -H "Content-Type: application/json" -d '{"text": "Conteúdo atualizado", "archived": false}'
```
**Deletar Publicação:**

```bash
curl -X DELETE http://localhost:8080/posts/{id}
```

**Arquivar Publicação:**

```bash
curl -X PATCH http://localhost:8080/posts/{id}/archive
```

**Desarquivar Publicação:**

```bash
curl -X PATCH http://localhost:8080/posts/{id}/unarchive
```

**Listar Comentários de um Post:**

```bash
curl -X GET http://localhost:8080/posts/{id}/comments
```

###  Comments (`/comments`)


**Criar Comentário:**

```bash
curl -X POST http://localhost:8080/comments -H "Content-Type: application/json" -d '{"userId": "{user_id}", "postId": "{post_id}", "message": "Excelente post!"}'
```

**Atualizar Comentário:**

```bash
curl -X PUT http://localhost:8080/comments/{id} -H "Content-Type: application/json" -d '{"message": "Comentário editado"}'
```

**Deletar Comentário:**

```bash
curl -X DELETE http://localhost:8080/comments/{id}
```

# Demonstrações de uso dos principais endpoints

## 1. 🚀 Fluxo de Onboarding (Usuário e Post)

Neste fluxo, registramos um novo perfil e criamos o primeiro conteúdo.  
A senha é enviada no cadastro, mas **nunca retornada na resposta**.

---

### 👤 A. Criar um Novo Usuário

**Endpoint:** `POST /users`  

**Descrição:** Registra um usuário e valida unicidade de e-mail/username.

#### 🔧 Chamada cURL

```bash
curl -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-d '{
  "username": "zematheus",
  "name": "José Matheus",
  "email": "jose@teste.com",
  "password": "senha_secreta_123",
  "biography": "Software Engineer focado em Java"
}'
```

#### ✅ Resposta (201 Created)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "username": "zematheus",
  "name": "José Matheus",
  "email": "jose@teste.com",
  "biography": "Software Engineer focado em Java"
}
```

> 🔐 **Nota de Segurança:** O campo `password` é filtrado pelo `UserWebMapper` e não aparece no JSON.

---

### 📝 B. Criar uma Publicação (Post)

**Endpoint:** `POST /posts`  

**Descrição:** Vincula um novo texto ao ID do usuário criado.

#### 🔧 Chamada cURL

```bash
curl -X POST http://localhost:8080/posts \
-H "Content-Type: application/json" \
-d '{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "text": "Explorando os benefícios da Arquitetura....,
  "archived": false
}'
```

---

## 2. ⚠️ Validação de Regra de Negócio (Post Arquivado)

A API impede interações em conteúdos que não estão mais ativos.

---

### 🚫 Cenário: Tentativa de Comentário em Post Arquivado

#### 🔄 Arquivar Post

**Endpoint:** `PATCH /posts/{id}/archive`

---

### 💬 Tentativa de Comentário

**Endpoint:** `POST /comments`

#### 📥 Payload

```json
{
  "userId": "uuid-do-comentador",
  "postId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "Excelente reflexão!"
}
```

#### ❌ Resposta de Erro (400 Bad Request)

```json
{
  "timestamp": "2026-04-04T23:45:12Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Não é permitido adicionar comentários em publicações arquivadas.",
  "path": "/comments"
}
```

---

## 3. 🔎 Consultas de Interação Social

Endpoints otimizados para recuperar o grafo de interações entre usuários.

---

### 💬 Listar Comentários de uma Publicação

**Endpoint:** `GET /posts/{id}/comments`

#### ✅ Resposta Esperada

```json
[
  {
    "id": "uuid-comment-1",
    "userId": "uuid-user-x",
    "message": "Muito bom esse artigo!",
    "createdAt": "2026-04-04T20:00:00Z"
  },
  {
    "id": "uuid-comment-2",
    "userId": "uuid-user-y",
    "message": "Concordo com os pontos levantados.",
    "createdAt": "2026-04-04T21:15:00Z"
  }
]
```

---

## 4. 📛 Tratamento de Erros Padronizado (RFC 7807)

Qualquer falha de busca ou regra de negócio retorna um objeto padronizado  
para facilitar o tratamento no Front-end.

---

### ❌ Exemplo: Buscar Usuário Inexistente

**Endpoint:** `GET /users/00000000-0000-0000-0000-000000000000`

#### 🚫 Resposta (404 Not Found)

```json
{
  "timestamp": "2026-04-04T23:50:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Usuário não encontrado com o ID fornecido.",
  "path": "/users/00000000..."
}
```
