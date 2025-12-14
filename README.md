# JWT Validator API

API REST desenvolvida em Java 21 com Spring Boot para validação de tokens JWT conforme regras de negócio específicas.

## 📋 Descrição

Esta aplicação expõe um endpoint REST que recebe um token JWT como parâmetro e valida se ele atende às seguintes regras:

1. **Estrutura válida**: Deve ser um JWT válido (3 partes separadas por ponto)
2. **Quantidade de claims**: Deve conter exatamente 3 claims (Name, Role, Seed)
3. **Claim Name**:
    - Não pode conter caracteres numéricos
    - Tamanho máximo de 256 caracteres
4. **Claim Role**: Deve ser um dos valores: `Admin`, `Member` ou `External`
5. **Claim Seed**: Deve ser um número primo

## 🚀 Como Executar

### Pré-requisitos

- Java 21 ou superior
- Maven 3.6+ ou superior

### Executando a aplicação

1. Clone o repositório:
```bash
git clone https://github.com/jurineide/challenge.git
cd challenge
```

2. Compile o projeto:
```bash
./mvnw clean install
```

3. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Executando os testes

```bash
./mvnw test
```

## 📚 Documentação da API

### Swagger UI

Após iniciar a aplicação, acesse a documentação interativa em:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### Endpoint

#### Validar JWT

**GET** `/api/v1/jwt/validate`

Valida um token JWT conforme as regras de negócio.

**Parâmetros:**
- `jwt` (query parameter, obrigatório): Token JWT a ser validado

**Resposta de Sucesso (200 OK):**
```json
{
  "valid": true,
  "errorCode": null
}
```

**Resposta de Erro (200 OK):**
```json
{
  "valid": false,
  "errorCode": "ERR001"
}
```

> **Nota de Segurança:** Por questões de segurança, apenas o código de erro é retornado na resposta da API. A descrição detalhada do erro está disponível apenas na documentação abaixo para desenvolvedores.

**Exemplo de requisição:**
```bash
curl "http://localhost:8080/api/v1/jwt/validate?jwt=eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"
```

**Resposta (válido):**
```json
{
  "valid": true,
  "errorCode": null
}
```

## 📋 Códigos de Erro

Quando a validação falha (`valid: false`), a API retorna um código de erro específico que identifica o tipo de problema encontrado.

> **🔒 Segurança:** Por questões de segurança, apenas o código de erro é retornado na resposta da API. A descrição detalhada está disponível apenas nesta documentação para desenvolvedores, evitando expor informações sensíveis sobre a estrutura interna da validação.

A tabela abaixo descreve todos os códigos de erro possíveis:

| Código | Descrição |
|--------|-----------|
| **ERR001** | JWT inválido - estrutura do token está incorreta ou não pode ser parseada |
| **ERR002** | Quantidade de claims incorreta - o JWT deve conter exatamente 3 claims |
| **ERR003** | Claim Name contém caracteres numéricos |
| **ERR004** | Claim Name excede o tamanho máximo de 256 caracteres |
| **ERR005** | Claim Role inválida - deve ser um dos valores: Admin, Member ou External |
| **ERR006** | Claim Seed não é um número primo |
| **ERR007** | Claim Seed não é um número válido |
| **ERR008** | Claim obrigatória ausente |
| **ERR009** | Erro inesperado durante a validação |

## 🧪 Casos de Teste

### Caso 1: JWT Válido ✅
**Entrada:**
```
eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg
```

**Saída:**
```json
{
  "valid": true,
  "errorCode": null
}
```

**Justificativa:** O JWT contém exatamente 3 claims válidas:
- Role: "Admin" ✅
- Seed: "7841" (número primo) ✅
- Name: "Toninho Araujo" (sem números) ✅

### Caso 2: JWT Inválido (Estrutura) ❌
**Entrada:**
```
eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg
```

**Saída:**
```json
{
  "valid": false,
  "errorCode": "ERR001"
}
```

**Justificativa:** JWT com estrutura inválida (não pode ser parseado corretamente)

### Caso 3: Name com Números ❌
**Entrada:**
```
eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs
```

**Saída:**
```json
{
  "valid": false,
  "errorCode": "ERR003"
}
```

**Justificativa:** A claim Name contém o caractere numérico "4" em "M4ria Olivia"

### Caso 4: Mais de 3 Claims ❌
**Entrada:**
```
eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY
```

**Saída:**
```json
{
  "valid": false,
  "errorCode": "ERR002"
}
```

**Justificativa:** O JWT contém 4 claims (Role, Org, Seed, Name) ao invés de exatamente 3

## 🏗️ Arquitetura

A aplicação foi desenvolvida seguindo os princípios **SOLID** e boas práticas de **Programação Orientada a Objetos**:

### Estrutura de Pacotes

```
com.itau.challenge/
├── controller/          # Camada de apresentação (REST endpoints)
├── service/            # Lógica de negócio
├── validator/          # Validadores específicos (SRP)
├── model/              # DTOs e entidades
├── exception/          # Tratamento de exceções
├── config/             # Configurações (Swagger, etc)
└── util/               # Utilitários (primos, etc)
```

### Princípios SOLID Aplicados

1. **Single Responsibility Principle (SRP)**
    - Cada validador (`NameValidator`, `RoleValidator`, `SeedValidator`, `ClaimCountValidator`) tem uma única responsabilidade
    - `JwtStructureValidator` é responsável apenas pela validação estrutural do JWT
    - `PrimeNumberUtil` é responsável apenas pela verificação de números primos

2. **Open/Closed Principle (OCP)**
    - Novos validadores podem ser adicionados implementando a interface `ClaimValidator` sem modificar código existente
    - O `JwtValidationService` está aberto para extensão através da injeção de novos validadores

3. **Liskov Substitution Principle (LSP)**
    - Todos os validadores implementam `ClaimValidator` e podem ser substituídos sem quebrar o comportamento

4. **Interface Segregation Principle (ISP)**
    - A interface `ClaimValidator` é específica e focada apenas no que é necessário para validação de claims

5. **Dependency Inversion Principle (DIP)**
    - `JwtValidationService` depende da abstração `ClaimValidator` (interface), não de implementações concretas
    - Dependências são injetadas via construtor (Dependency Injection do Spring)

### Padrões de Design

- **Strategy Pattern**: Validadores como estratégias intercambiáveis
- **Chain of Responsibility**: Service orquestra validadores em sequência
- **Dependency Injection**: Spring Framework para injeção de dependências

## 📝 Detalhes dos Métodos

### `JwtValidationService.validate(String jwtToken)`
Orquestra toda a validação do JWT. Executa as validações em ordem fail-fast:
1. Valida estrutura do JWT e faz parsing
2. Valida quantidade de claims (fail-fast)
3. Valida cada claim específica (Name, Role, Seed)

**Retorno:** `boolean` - `true` se válido, `false` caso contrário

### `JwtStructureValidator.validateAndParse(String jwtToken)`
Valida a estrutura básica do JWT e retorna as claims parseadas.

**Retorno:** `Map<String, Object>` - Claims do JWT

**Exceções:** `InvalidJwtException` se o JWT for estruturalmente inválido

### `ClaimValidator.validate(Map<String, Object> claims)`
Interface implementada por todos os validadores de claims. Cada validador verifica uma regra específica.

**Exceções:** `ValidationException` se a validação falhar

### `PrimeNumberUtil.isPrime(long number)`
Verifica se um número é primo usando algoritmo otimizado (verifica apenas até √n).

**Retorno:** `boolean` - `true` se o número é primo

## 🔍 Observability

A aplicação inclui:

- **Logging estruturado** com SLF4J/Logback
- **Logs em pontos críticos**: entrada de requisições, validações, erros
- **UUID por transação** para rastreabilidade completa
- **Spring Boot Actuator** para health checks e métricas
    - Health: `http://localhost:8080/actuator/health`
    - Metrics: `http://localhost:8080/actuator/metrics`
    - Prometheus: `http://localhost:8080/actuator/prometheus`

### Métricas Customizadas

A aplicação expõe as seguintes métricas customizadas via Micrometer:

- **`jwt.validation.total`**: Contador total de validações realizadas
- **`jwt.validation.success`**: Contador de validações bem-sucedidas
- **`jwt.validation.error`**: Contador de validações falhadas (com tag `error_code`)
- **`jwt.validation.duration`**: Timer com duração das validações (percentis: p50, p95, p99)

#### Exemplo de Consulta de Métricas

```bash
# Listar todas as métricas disponíveis
curl http://localhost:8080/actuator/metrics

# Ver métrica específica
curl http://localhost:8080/actuator/metrics/jwt.validation.total
curl http://localhost:8080/actuator/metrics/jwt.validation.success
curl http://localhost:8080/actuator/metrics/jwt.validation.error

# Ver métricas no formato Prometheus
curl http://localhost:8080/actuator/prometheus
```

#### Exemplo de Métricas Prometheus

```
# HELP jwt_validation_total Total number of JWT validations
# TYPE jwt_validation_total counter
jwt_validation_total 150.0

# HELP jwt_validation_success Number of successful JWT validations
# TYPE jwt_validation_success counter
jwt_validation_success 120.0

# HELP jwt_validation_error Number of failed JWT validations
# TYPE jwt_validation_error counter
jwt_validation_error{error_code="ERR001"} 10.0
jwt_validation_error{error_code="ERR003"} 15.0
jwt_validation_error{error_code="ERR002"} 5.0

# HELP jwt_validation_duration_seconds Time taken to validate JWT
# TYPE jwt_validation_duration_seconds summary
jwt_validation_duration_seconds_count 150.0
jwt_validation_duration_seconds_sum 2.5
jwt_validation_duration_seconds_max 0.05
```

### Níveis de Log

- **INFO**: Operações principais (início/fim de validação, resultados)
- **WARN**: Validações que falharam
- **ERROR**: Erros inesperados

## 🧪 Testes

A aplicação possui cobertura de testes abrangente:

- **Testes Unitários**: Cada validador e utilitário testado isoladamente
- **Testes de Integração**: Service e Controller testados com casos reais
- **Casos de Teste**: Todos os 4 casos fornecidos estão cobertos

### Executar testes com cobertura

```bash
./mvnw test
```

## 🤔 Premissas e Decisões Arquiteturais

### Premissas Assumidas

1. **Validação de Assinatura**: O JWT não precisa ser assinado/verificado criptograficamente. Apenas a estrutura e o conteúdo das claims são validados.

2. **Formato do Seed**: A claim Seed pode ser uma string numérica que será convertida para `long` para validação de número primo.

3. **Caracteres Especiais no Name**: A claim Name aceita espaços e caracteres especiais (acentos, hífens, etc.), apenas não aceita números.

4. **Case Sensitivity**: A validação de Role é case-sensitive ("Admin" é válido, mas "admin" não é).

5. **Resposta da API**: A API retorna HTTP 200 OK com um objeto JSON contendo o campo `valid` (boolean), mesmo quando o JWT é inválido. Erros de validação são tratados como resultado da validação, não como erros HTTP.

### Decisões Arquiteturais

1. **Fail-Fast**: Validações são executadas em ordem de custo computacional (estrutura → count → claims específicas) para retornar erros rapidamente.

2. **Imutabilidade**: DTOs são imutáveis onde possível, usando Lombok para reduzir boilerplate.

3. **Tratamento de Exceções**: Exceções customizadas (`InvalidJwtException`, `ValidationException`) permitem tratamento diferenciado e logs mais informativos.

4. **Biblioteca JWT**: Utilizada a biblioteca JJWT (io.jsonwebtoken) versão 0.12.5, que é padrão no ecossistema Java e suporta parsing sem verificação de assinatura.

5. **Algoritmo de Números Primos**: Implementado algoritmo otimizado que verifica apenas até √n, reduzindo complexidade de O(n) para O(√n).

6. **Documentação**: Swagger/OpenAPI integrado para documentação interativa da API, facilitando testes e integração.

## 📦 Dependências Principais

- **Spring Boot 4.0.0**: Framework principal
- **JJWT 0.12.5**: Parsing de tokens JWT
- **SpringDoc OpenAPI 2.5.0**: Documentação Swagger
- **Lombok**: Redução de boilerplate
- **JUnit 5**: Framework de testes
- **Mockito**: Mocking para testes

## 🔧 Configuração

As configurações principais estão em `src/main/resources/application.properties`:

- Porta do servidor: 8080
- Níveis de log configuráveis
- Endpoints do Actuator expostos
- Configuração do Swagger



## 📄 Licença

Este projeto foi desenvolvido como parte de um desafio técnico.

## 👤 Autor
Jurineide  
Desenvolvido seguindo princípios enterprise e boas práticas de desenvolvimento.

