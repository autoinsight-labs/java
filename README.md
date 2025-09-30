# AutoInsight API

## 🚀 Sobre o Projeto

A **AutoInsight API** é uma aplicação Spring Boot que expõe APIs RESTful para gestão de usuários, papéis, veículos e reservas (bookings), além de telas web (Thymeleaf) para operações manuais. O projeto utiliza Oracle Database, migrações Flyway, autenticação via JWT, cache e paginação.

## 👥 Equipe de Desenvolvimento

| Nome | RM | E-mail | GitHub | LinkedIn |
|------|-------|---------|---------|----------|
| Arthur Vieira Mariano | RM554742 | arthvm@proton.me | [@arthvm](https://github.com/arthvm) | [arthvm](https://linkedin.com/in/arthvm/) |
| Guilherme Henrique Maggiorini | RM554745 | guimaggiorini@gmail.com | [@guimaggiorini](https://github.com/guimaggiorini) | [guimaggiorini](https://linkedin.com/in/guimaggiorini/) |
| Ian Rossato Braga | RM554989 | ian007953@gmail.com | [@iannrb](https://github.com/iannrb) | [ianrossato](https://linkedin.com/in/ianrossato/) |

## 🛠️ Tecnologias Utilizadas

- **Java 17**, **Spring Boot 3.4.5**
- **Spring Web**, **Spring Data JPA** (Oracle)
- **Spring Security** com **JWT**
- **Bean Validation (Jakarta)**
- **Thymeleaf** (camada web)
- **Flyway** para migrações (`src/main/resources/db/migration`)
- **Spring Cache** e paginação do Spring Data
- **Lombok 1.18.38**
- **CUID 2.0.3** (IDs)
- **spring-dotenv 4.0.0** (variáveis de ambiente)
- **OpenAPI/Swagger** via `springdoc-openapi`

## 📦 Estrutura do Projeto

- `br/com/autoinsight/autoinsight_client/modules/*`: domínios (`auth`, `users`, `roles`, `vehicles`, `bookings`)
  - `controllers`: APIs REST sob `/api/*` e controllers de view sob `/view/*`
  - `useCases`, `services`, `repositories`, `dto`, `mapper`, `entities`
- `config`: `SecurityConfig`, `SwaggerConfig`, etc.
- `resources/templates`: páginas Thymeleaf
- `resources/static`: assets
- `resources/db/migration`: scripts Flyway (V1...V4)

## 🔐 Segurança e Autenticação

- APIs REST sob `/api/**` são protegidas por JWT.
- Endpoints públicos:
  - `/api/auth/**` (ex.: `POST /api/auth/login`)
  - `/api/users/register`
- Rotas Web (Thymeleaf):
  - Livre: `/`, `/login`, assets (`/css/**`, `/js/**`)
  - Protegido: `/view/**` (com exceção de `/view/roles/**` que exige `ROLE_ADM`)
- Após autenticar, enviar o header: `Authorization: Bearer <token>`.

## 📜 Documentação da API (Swagger)

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Esquema de segurança: Bearer JWT (configurado em `SwaggerConfig`)

## 🗄️ Banco de Dados e Migrações

- Banco: Oracle (dialeto `org.hibernate.dialect.OracleDialect`)
- DDL gerenciado por Flyway (Spring JPA `ddl-auto=none`)
- Migrações em `classpath:db/migration`:
  - `V1__Drop_all_tables.sql`
  - `V2__Create_all_tables.sql`
  - `V3__Insert_initial_data.sql`
  - `V4__Create_indexes.sql`

## ⚙️ Configuração

Variáveis de ambiente (exemplo `.env`):

```bash
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver
```

Principais propriedades (já referenciadas no `application.properties`):
- `spring.flyway.enabled=true`
- `spring.flyway.locations=classpath:db/migration`
- `spring.jpa.hibernate.ddl-auto=none`

## 🚀 Como Executar Localmente

1. Clone o repositório
```bash
git clone https://github.com/autoinsight-labs/java.git
cd java
```

2. **Configure as variáveis de ambiente:**
   ```bash
   # Crie um arquivo .env na raiz do projeto
   cat > .env << EOF
   SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid>
   SPRING_DATASOURCE_USERNAME=<username>
   SPRING_DATASOURCE_PASSWORD=<password>
   SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver
   EOF
   ```

3. **Compile o projeto:**
   ```bash
   mvn clean compile
   ```

4. **Execute o projeto:**
   ```bash
   mvn spring-boot:run
   ```

## 🐳 Execução com Docker

Build da imagem e execução:
```bash
docker build -t autoinsight-api .

docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="$SPRING_DATASOURCE_URL" \
  -e SPRING_DATASOURCE_USERNAME="$SPRING_DATASOURCE_USERNAME" \
  -e SPRING_DATASOURCE_PASSWORD="$SPRING_DATASOURCE_PASSWORD" \
  -e SPRING_DATASOURCE_DRIVERCLASSNAME="$SPRING_DATASOURCE_DRIVERCLASSNAME" \
  autoinsight-api
```

## 🔑 Autenticação (Fluxo)

- Registrar usuário: `POST /api/users/register`
- Login: `POST /api/auth/login` → retorna JWT
- Usar JWT nas demais rotas `/api/**` via `Authorization: Bearer <token>`

## 📋 Endpoints Principais

### Autenticação
- `POST /api/auth/login` — autentica e retorna JWT
- `GET /api/auth/me` — dados do usuário autenticado

### Usuários
- `POST /api/users/register` — cria usuário (público)
- `GET /api/users/{id}` — obter por ID
- `PUT /api/users/{id}` — atualizar
- `DELETE /api/users/{id}` — excluir

### Papéis (roles) [requer `ROLE_ADM`]
- `GET /api/roles/`
- `GET /api/roles/{id}`
- `POST /api/roles/`
- `PUT /api/roles/{id}`
- `DELETE /api/roles/{id}`

### Veículos
- `GET /api/vehicles/` — lista paginada
- `GET /api/vehicles/{id}`
- `GET /api/vehicles/user/{userId}`
- `POST /api/vehicles/`
- `PUT /api/vehicles/{id}`
- `DELETE /api/vehicles/{id}`

### Reservas (bookings)
- `GET /api/bookings/` — lista paginada (ordenada por `occursAt` desc.)
- `GET /api/bookings/{id}`
- `GET /api/bookings/yard/{yardId}` — paginado
- `GET /api/bookings/vehicle/{vehicleId}` — paginado
- `POST /api/bookings/`
- `PUT /api/bookings/{id}`
- `DELETE /api/bookings/{id}`

## 📄 Licença

Projeto acadêmico desenvolvido no challenge da FIAP.