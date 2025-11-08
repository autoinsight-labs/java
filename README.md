# AutoInsight API

## 🚀 Sobre o Projeto

A **AutoInsight API** é uma aplicação Spring Boot que expõe APIs RESTful para gestão de usuários, papéis, veículos e reservas (bookings), além de telas web (Thymeleaf) para operações manuais. O projeto utiliza Oracle Database, migrações Flyway, autenticação via JWT, cache e paginação.

## 🎥 Vídeo Demonstrativo

Assista ao vídeo demonstrativo da solução: [AutoInsight - Demonstração](https://youtu.be/8q-QXujG43I)

## 👥 Equipe de Desenvolvimento

| Nome                        | RM      | Turma    | E-mail                 | GitHub                                         | LinkedIn                                   |
|-----------------------------|---------|----------|------------------------|------------------------------------------------|--------------------------------------------|
| Arthur Vieira Mariano       | RM554742| 2TDSPF   | arthvm@proton.me       | [@arthvm](https://github.com/arthvm)           | [arthvm](https://linkedin.com/in/arthvm/)  |
| Guilherme Henrique Maggiorini| RM554745| 2TDSPF  | guimaggiorini@gmail.com| [@guimaggiorini](https://github.com/guimaggiorini) | [guimaggiorini](https://linkedin.com/in/guimaggiorini/) |
| Ian Rossato Braga           | RM554989| 2TDSPY   | ian007953@gmail.com    | [@iannrb](https://github.com/iannrb)           | [ianrossato](https://linkedin.com/in/ianrossato/)      |

## 🛠️ Tecnologias Utilizadas

- **Java 17**, **Spring Boot 3.4.5**
- **Spring Web**, **Spring Data JPA** (Oracle)
- **Spring Security** com **JWT** (jjwt 0.12.3)
- **Bean Validation (Jakarta)**
- **Thymeleaf** (camada web)
- **Flyway** para migrações (`src/main/resources/db/migration`)
- **Spring Cache** e paginação do Spring Data
- **Lombok 1.18.38**
- **CUID 2.0.3** (IDs únicos)
- **spring-dotenv 4.0.0** (variáveis de ambiente)
- **OpenAPI/Swagger** via `springdoc-openapi 2.8.6`
- **Oracle JDBC Driver 19.8.0.0**
- **BCrypt** para hash de senhas

## 📦 Estrutura do Projeto

- `br/com/autoinsight/autoinsight_client/modules/*`: domínios (`auth`, `users`, `roles`, `vehicles`, `bookings`)
  - `controllers`: APIs REST sob `/api/*` e controllers de view sob `/view/*`
  - `useCases`, `services`, `repositories`, `dto`, `mapper`, `entities`
- `config`: `SecurityConfig`, `SwaggerConfig`, etc.
- `resources/templates`: páginas Thymeleaf
- `resources/static`: assets
- `resources/db/migration`: scripts Flyway (V1...V4)

## 🔐 Segurança e Autenticação

O projeto utiliza **duas configurações de segurança separadas**:

### APIs REST (`/api/**`)
- Protegidas por **JWT Bearer Token**
- **Endpoints públicos:**
  - `/api/auth/**` (login e autenticação)
  - `/api/users/register` (registro de usuários)
- **Endpoints protegidos:**
  - `/api/roles/**` → Requer `ROLE_ADM`
  - Demais endpoints → Requer autenticação
- **Header obrigatório:** `Authorization: Bearer <token>`

### Interface Web (Thymeleaf)
- Autenticação via **formulário de login**
- **Rotas livres:** `/`, `/login`
- **Rotas protegidas:**
  - `/view/**` → Requer autenticação
  - `/view/roles/**` → Requer `ROLE_ADM`
- **Sessões:** Gerenciadas pelo Spring Security

## 📜 Documentação da API (Swagger)

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Esquema de segurança: Bearer JWT (configurado em `SwaggerConfig`)

## 🗄️ Banco de Dados e Migrações

- **Banco:** Oracle Database (dialeto `org.hibernate.dialect.OracleDialect`)
- **DDL:** Gerenciado exclusivamente pelo Flyway (`spring.jpa.hibernate.ddl-auto=none`)
- **Migrações** em `src/main/resources/db/migration`:
  - `V1__Drop_all_tables.sql` — Remove tabelas existentes
  - `V2__Create_all_tables.sql` — Cria estrutura das tabelas
  - `V3__Insert_initial_data.sql` — Dados iniciais (roles, usuários)
  - `V4__Create_indexes.sql` — Índices para performance

### Entidades Principais
- **UsersEntity** — Usuários do sistema
- **RoleEntity** — Papéis/permissões (USER, ADM)
- **VehicleEntity** — Veículos com placas brasileiras
- **BookingEntity** — Reservas de pátios com validação temporal

## ⚙️ Configuração

Variáveis de ambiente (exemplo `.env`):

```bash
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver
```

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

## 🔑 Fluxo de Autenticação

### Para APIs REST
1. **Registrar usuário:** `POST /api/users/register`
2. **Fazer login:** `POST /api/auth/login` → retorna JWT
3. **Usar token:** Incluir `Authorization: Bearer <token>` nos headers
4. **Verificar perfil:** `GET /api/auth/me` → dados do usuário autenticado

### Para Interface Web
1. **Acessar:** `http://localhost:8080/login`
2. **Credenciais padrão:** Definidas em `V3__Insert_initial_data.sql`
3. **Navegação:** Após login, acessar `/view/*` conforme permissões

## 📋 Endpoints Principais

### 🔑 Autenticação (`/api/auth`)
- `POST /api/auth/login` — autentica e retorna JWT
- `GET /api/auth/me` — dados do usuário autenticado

### 👥 Usuários (`/api/users`)
- `POST /api/users/register` — cria usuário (público)
- `GET /api/users` — lista paginada (autenticado)
- `GET /api/users/{id}` — obter por ID
- `PUT /api/users/{id}` — atualizar
- `DELETE /api/users/{id}` — excluir

### 🎭 Papéis (`/api/roles`) [requer `ROLE_ADM`]
- `GET /api/roles` — lista paginada
- `GET /api/roles/{id}` — obter por ID
- `POST /api/roles` — criar papel
- `PUT /api/roles/{id}` — atualizar
- `DELETE /api/roles/{id}` — excluir

### 🚗 Veículos (`/api/vehicles`)
- `GET /api/vehicles` — lista paginada
- `GET /api/vehicles/{id}` — obter por ID
- `GET /api/vehicles/user/{userId}` — veículo do usuário
- `POST /api/vehicles` — criar veículo
- `PUT /api/vehicles/{id}` — atualizar
- `DELETE /api/vehicles/{id}` — excluir

### 📅 Reservas (`/api/bookings`)
- `GET /api/bookings` — lista paginada (ordenada por `occursAt` desc.)
- `GET /api/bookings/{id}` — obter por ID
- `GET /api/bookings/yard/{yardId}` — reservas por pátio (paginado)
- `GET /api/bookings/vehicle/{vehicleId}` — reservas por veículo (paginado)
- `POST /api/bookings` — criar reserva
- `PUT /api/bookings/{id}` — atualizar
- `DELETE /api/bookings/{id}` — excluir

## 🌐 Interface Web (Thymeleaf)

### Rotas de Visualização
- `/` — página inicial
- `/login` — formulário de login

### Gestão via Interface Web (`/view/*`)
- `/view/users` — gerenciar usuários
- `/view/vehicles` — gerenciar veículos  
- `/view/bookings` — gerenciar reservas
- `/view/roles` — gerenciar papéis (apenas `ROLE_ADM`)

Cada módulo possui páginas para **listar**, **criar** e **editar** registros.

## 📄 Licença

Projeto acadêmico desenvolvido no challenge da FIAP.