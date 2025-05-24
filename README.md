# AutoInsight API

## 🚀 Sobre o Projeto

A **AutoInsight API** é uma API RESTful desenvolvida em Spring Boot para gestão inteligente de veículos e reservas de motocicletas. A API fornece endpoints completos para gerenciamento de veículos, reservas (bookings) e suas relações, com integração ao banco de dados Oracle, sistema de cache, validações robustas e paginação em todas as consultas de listagem.

## 👥 Equipe de Desenvolvimento

| Nome | RM | E-mail | GitHub | LinkedIn |
|------|-------|---------|---------|----------|
| Arthur Vieira Mariano | RM554742 | arthvm@proton.me | [@arthvm](https://github.com/arthvm) | [arthvm](https://linkedin.com/in/arthvm/) |
| Guilherme Henrique Maggiorini | RM554745 | guimaggiorini@gmail.com | [@guimaggiorini](https://github.com/guimaggiorini) | [guimaggiorini](https://linkedin.com/in/guimaggiorini/) |
| Ian Rossato Braga | RM554989 | ian007953@gmail.com | [@iannrb](https://github.com/iannrb) | [ianrossato](https://linkedin.com/in/ianrossato/) |

## 🛠️ Tecnologias Utilizadas

### Stack Principal
- **Java 17** - Linguagem de programação
- **Spring Boot 3.4.5** - Framework principal
- **Spring Web** - Criação da API REST
- **Spring Data JPA** - ORM para acesso ao banco de dados
- **Oracle Database** - Banco de dados principal (com driver OJDBC8 19.8.0.0)
- **Lombok 1.18.38** - Redução de boilerplate code
- **Bean Validation (Jakarta)** - Validação de campos
- **Spring Cache** - Sistema de cache
- **Spring Data Pagination** - Sistema de paginação automática

### Dependências Adicionais
- **CUID 2.0.3** - Geração de IDs únicos
- **Spring DotEnv 4.0.0** - Gerenciamento de variáveis de ambiente
- **Maven** - Gerenciamento de dependências

### Arquitetura
- **Clean Architecture** - Separação em camadas (entities, use cases, controllers)
- **Repository Pattern** - Abstração de acesso a dados
- **DTOs** - Transferência de dados
- **Exception Handler** - Tratamento centralizado de erros
- **Paginação Uniforme** - Todas as consultas de listagem utilizam paginação

## 🗄️ Estrutura do Banco de Dados

O projeto utiliza Spring Data JPA com Oracle Database e inclui as seguintes entidades:

### **Vehicles** (Veículos)
- `id` (String) - Identificador único
- `plate` (String) - Placa do veículo (formato brasileiro)
- `model_id` (String) - ID do modelo (referência externa)
- `user_id` (String) - ID do usuário proprietário

### **Bookings** (Reservas)
- `id` (String) - Identificador único
- `vehicle_id` (String) - ID do veículo
- `yard_id` (String) - ID do pátio (referência externa)
- `occurs_at` (LocalDateTime) - Data e hora da reserva
- `cancelled_at` (LocalDateTime) - Data e hora do cancelamento (opcional)

### Relacionamentos
- **Booking** → **Vehicle**: Relacionamento ManyToOne (uma reserva pertence a um veículo)

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Maven 3.6+
- Oracle Database
- Git

### Instalação

1. **Clone o repositório:**
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

5. **Acesse a API:**
   - Aplicação: `http://localhost:8080`

## 📋 Endpoints da API

### 🏍️ Vehicles (Veículos)

| Método | Endpoint | Descrição | Parâmetros | Retorno |
|--------|----------|-----------|------------|---------|
| GET | `/vehicles/` | Lista todos os veículos **com paginação** | `page` (padrão: 0), `size` (padrão: 10) | 200 OK - `Page<VehicleDTO>` |
| GET | `/vehicles/{id}` | Busca veículo por ID | `id` (path) | 200 OK - `VehicleDTO`, 404 NotFound |
| GET | `/vehicles/user/{userId}` | Busca veículo por usuário | `userId` (path) | 200 OK - `VehicleDTO`, 404 NotFound |
| POST | `/vehicles/` | Cria novo veículo | Body: `VehicleDTO` | 200 OK, 400 BadRequest |
| PUT | `/vehicles/{id}` | Atualiza veículo | `id` (path), Body: `VehicleDTO` | 204 NoContent, 404 NotFound |
| DELETE | `/vehicles/{id}` | Remove veículo | `id` (path) | 204 NoContent, 404 NotFound |

### 📅 Bookings (Reservas)

| Método | Endpoint | Descrição | Parâmetros | Retorno |
|--------|----------|-----------|------------|---------|
| GET | `/bookings/` | Lista todas as reservas **com paginação** | `page` (padrão: 0), `size` (padrão: 10) | 200 OK - `Page<BookingDTO>` |
| GET | `/bookings/{id}` | Busca reserva por ID | `id` (path) | 200 OK - `BookingDTO`, 404 NotFound |
| GET | `/bookings/yard/{yardId}` | Busca reservas por pátio **com paginação** | `yardId` (path), `page`, `size` | 200 OK - `Page<BookingDTO>` |
| GET | `/bookings/vehicle/{vehicleId}` | Busca reservas por veículo **com paginação** | `vehicleId` (path), `page`, `size` | 200 OK - `Page<BookingDTO>` |
| POST | `/bookings/` | Cria nova reserva | Body: `BookingDTO` | 200 OK, 400 BadRequest |
| PUT | `/bookings/{id}` | Atualiza reserva | `id` (path), Body: `BookingDTO` | 204 NoContent, 404 NotFound |
| DELETE | `/bookings/{id}` | Remove reserva | `id` (path) | 204 NoContent, 404 NotFound |

### 📄 Estrutura de Resposta Paginada

Todas as rotas que retornam listas agora utilizam o formato padrão do Spring Data:

```json
{
  "content": [
    // Array com os itens da página atual
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false
  }
}
```

### 🔄 Parâmetros de Paginação

Todos os endpoints de listagem aceitam os seguintes parâmetros de query:

- **`page`** (int): Número da página (base 0) - Padrão: `0`
- **`size`** (int): Tamanho da página - Padrão: `10`

### 📊 Ordenação Automática

- **Bookings**: Todas as consultas são automaticamente ordenadas por `occursAt` em ordem **decrescente** (mais recentes primeiro)
- **Vehicles**: Ordenação padrão do banco de dados

## 📊 Exemplos de Uso

### Criar um Veículo
```bash
curl -X POST http://localhost:8080/vehicles/ \
  -H "Content-Type: application/json" \
  -d '{
    "plate": "ABC1234",
    "modelId": "a1b2c3d4e5f6789012345678",
    "userId": "z9y8x7w6v5u4321098765432"
  }'
```

### Criar uma Reserva
```bash
curl -X POST http://localhost:8080/bookings/ \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": "vehicle123456789012345678",
    "yardId": "yard123456789012345678",
    "occursAt": "2024-06-15 14:30"
  }'
```

### Listar Veículos com Paginação
```bash
# Primeira página com 10 itens
curl "http://localhost:8080/vehicles/?page=0&size=10"

# Segunda página com 5 itens
curl "http://localhost:8080/vehicles/?page=1&size=5"
```

### Listar Reservas com Paginação (ordenadas por data)
```bash
# Primeira página - reservas mais recentes primeiro
curl "http://localhost:8080/bookings/?page=0&size=10"

# Buscar reservas por pátio com paginação
curl "http://localhost:8080/bookings/yard/yard123456789012345678?page=0&size=5"

# Buscar reservas por veículo com paginação
curl "http://localhost:8080/bookings/vehicle/vehicle123456789012345678?page=0&size=3"
```

### Buscar um Veículo Específico (sem paginação)
```bash
curl "http://localhost:8080/vehicles/vehicle123456789012345678"
```

## 🎯 Funcionalidades de Negócio

### 🔧 Validações Implementadas
- **Placa de Veículo**: Formato brasileiro obrigatório
- **IDs Únicos**: Formato CUID2 para modelId, userId, vehicleId, yardId
- **Datas de Reserva**: Devem ser futuras
- **Datas de Cancelamento**: Devem ser passadas ou presentes
- **Veículo por Usuário**: Um usuário pode ter apenas um veículo
- **Placa Única**: Cada placa deve ser única no sistema
- **Reserva Única**: Não pode haver duas reservas para o mesmo veículo no mesmo horário

### 🏗️ Arquitetura em Camadas
- **Controllers**: Recebem requisições HTTP e delegam para use cases
- **Use Cases**: Contêm a lógica de negócio
- **Repositories**: Abstração para acesso aos dados
- **Entities**: Representam as tabelas do banco de dados
- **DTOs**: Objetos para transferência de dados
- **Mappers**: Conversão entre entities e DTOs

### 📈 Sistema de Cache
- Cache em memória para otimizar consultas frequentes
- Invalidação automática em operações de escrita
- Caches separados por tipo de consulta para maior eficiência
- **Cache paginado**: Otimização específica para consultas paginadas

### 📄 Sistema de Paginação
- **Paginação Universal**: Todas as rotas de listagem utilizam paginação automaticamente
- **Ordenação Inteligente**: Bookings ordenadas por data de ocorrência (decrescente)
- **Performance Otimizada**: Consultas limitadas para melhor performance
- **Flexibilidade**: Parâmetros configuráveis de página e tamanho
- **Compatibilidade**: Endpoints únicos que retornam dados paginados

## 🔧 Tratamento de Erros

O sistema possui tratamento centralizado de erros que retorna:

### Erros de Validação (400 Bad Request)
```json
[
  {
    "message": "Invalid Brazilian license plate format",
    "field": "plate"
  }
]
```

### Erros de Negócio
- **PlateFoundException**: Placa já cadastrada
- **UserVehicleFoundException**: Usuário já possui veículo
- **BookingFoundException**: Conflito de horário em reserva
- **EntityNotFoundException**: Recurso não encontrado

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos como parte do challenge da FIAP.