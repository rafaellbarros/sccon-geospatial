# SCCON Geospatial - API de Pessoas

## 📋 Sobre o Projeto

API REST desenvolvida com Spring Boot 3.5.13 para gerenciamento de pessoas, utilizando armazenamento em memória (`ConcurrentHashMap`) para alta performance nas operações de busca por ID.

### 🚀 Funcionalidades Implementadas

- ✅ **Modelo de Pessoa**: Entidade com atributos (`id`, `nome`, `dataNascimento`, `dataAdmissao`)
- ✅ **Armazenamento em Memória**: Mapa `ConcurrentHashMap` otimizado para busca por ID (O(1))
- ✅ **Inicialização Automática**: População inicial com 3 pessoas no momento da inicialização
- ✅ **Endpoint GET /person**: Retorna lista ordenada alfabeticamente por nome
- ✅ **Endpoint POST /person**: Inclui nova pessoa no mapa com regras de negócio específicas
  - ID não especificado → atribui automaticamente o maior ID + 1
  - ID especificado e já existente → retorna HTTP 409 (Conflict)
  - Validação de campos obrigatórios → HTTP 400 (Bad Request)
- ✅ **Endpoint DELETE /person/{id}**
  - Remove uma pessoa do mapa em memória
  - Caso o ID não exista → retorna HTTP 404 (Not Found)
- ✅ **Endpoint PUT /person/{id}**
  - Atualiza uma pessoa existente no mapa em memória
  - Caso o ID não exista → retorna HTTP 404 (Not Found)
  - Validação de campos obrigatórios → HTTP 400 (Bad Request)
- ✅ **Endpoint PATCH /person/{id}**
  - Atualização parcial de atributos
  - Caso o ID não exista → HTTP 404
  - Campos inválidos → HTTP 400
- ✅ **Endpoint GET /person/{id}**
  - Busca uma pessoa específica por ID
  - Caso o ID não exista → HTTP 404 (Not Found)

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.5.13
- Spring Web
- Maven

---

## 🔧 Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- Git (opcional)

---

## 🚀 Como Executar a Aplicação

### 1. Clone o repositório

```bash
git clone https://github.com/rafaellbarros/sccon-geospatial.git
cd sccon-geospatial
```

### 2. Execute a aplicação

#### Opção 1: Usando Maven

```bash
mvn clean install
mvn spring-boot:run
```

#### Opção 2: Gerando JAR e executando

```bash
mvn clean package
java -jar target/sccon-geospatial-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Endpoints Disponíveis

### Base URL

```bash
http://localhost:8080/person
```

---

## 1. Listar todas as pessoas (ordenadas por nome - A-Z)

```bash
GET /person
```

Exemplo:

```bash
curl -X GET http://localhost:8080/person
```

---

## 2. Criar pessoa

```bash
POST /person
```

---

### 2.1 Criar pessoa sem ID (ID automático)

Quando o campo `id` não é informado, a API atribui automaticamente o próximo ID disponível.

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Mariana Costa",
    "data_nascimento": "1993-09-18",
    "data_admissao": "2019-04-22"
  }'
```

### Resposta esperada — HTTP 201 (Created)

```json
{
  "id": 4,
  "nome": "Mariana Costa",
  "data_nascimento": "1993-09-18",
  "data_admissao": "2019-04-22"
}
```

---

### 2.2 Criar pessoa com ID informado e disponível

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "id": 10,
    "nome": "Fernanda Souza",
    "data_nascimento": "1991-07-30",
    "data_admissao": "2017-11-15"
  }'
```

### Resposta esperada — HTTP 201 (Created)

```json
{
  "id": 10,
  "nome": "Fernanda Souza",
  "data_nascimento": "1991-07-30",
  "data_admissao": "2017-11-15"
}
```

---

### 2.3 Tentar criar pessoa com ID já existente (Conflito)

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nome": "Conflito Teste",
    "data_nascimento": "1990-01-01",
    "data_admissao": "2020-01-01"
  }'
```

### Resposta esperada — HTTP 409 (Conflict)

```json
{
  "timestamp": "2026-04-01T19:17:46",
  "status": 409,
  "error": "Conflict",
  "message": "Já existe uma pessoa com o ID 1. Nome: João Silva"
}
```

---

### 2.4 Campos obrigatórios ausentes

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "",
    "data_nascimento": null,
    "data_admissao": null
  }'
```

### Resposta esperada — HTTP 400 (Bad Request)

```json
{
  "timestamp": "2026-04-01T19:20:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Nome é obrigatório"
}
```

---

## 3. Remover pessoa por ID

```bash
DELETE /person/{id}
```

---

### 3.1 Remover pessoa existente

```bash
curl -X DELETE http://localhost:8080/person/1
```

### Resposta esperada — HTTP 204 (No Content)

```http
204 No Content
```

---

### 3.2 Tentar remover pessoa inexistente

```bash
curl -X DELETE http://localhost:8080/person/999
```

### Resposta esperada — HTTP 404 (Not Found)

```json
{
  "timestamp": "2026-04-01T19:40:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pessoa com ID 999 não encontrada"
}
```

---

## 4. Atualizar pessoa por ID

```bash
PUT /person/{id}
```

Atualiza completamente os dados de uma pessoa existente no mapa em memória.

Caso o ID informado não exista, a API retorna **HTTP 404 (Not Found)**.

---

### 4.1 Atualizar pessoa existente

```bash
curl -X PUT http://localhost:8080/person/2 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Oliveira Atualizada",
    "data_nascimento": "1985-08-22",
    "data_admissao": "2010-07-01"
  }'
```

### Resposta esperada — HTTP 200 (OK)

```json
{
  "id": 2,
  "nome": "Maria Oliveira Atualizada",
  "data_nascimento": "1985-08-22",
  "data_admissao": "2010-07-01"
}
```

---

### 4.2 Tentar atualizar pessoa inexistente

```bash
curl -X PUT http://localhost:8080/person/999 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pessoa Inexistente",
    "data_nascimento": "1990-01-01",
    "data_admissao": "2020-01-01"
  }'
```

### Resposta esperada — HTTP 404 (Not Found)

```json
{
  "timestamp": "2026-04-01T20:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pessoa com ID 999 não encontrada"
}
```

---

### 4.3 Tentar atualizar com campos inválidos

```bash
curl -X PUT http://localhost:8080/person/2 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "",
    "data_nascimento": null,
    "data_admissao": null
  }'
```

### Resposta esperada — HTTP 400 (Bad Request)

```json
{
  "timestamp": "2026-04-01T20:05:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Nome é obrigatório"
}
```

---

## 5. Atualizar atributo(s) de uma pessoa por ID

```bash
PATCH /person/{id}
```

Atualiza parcialmente um ou mais atributos de uma pessoa existente no mapa em memória.

Caso o ID informado não exista, a API retorna **HTTP 404 (Not Found)**.

---

### 5.1 Atualizar apenas o nome

```bash
curl -X PATCH http://localhost:8080/person/2 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Oliveira Silva"
  }'
```

### Resposta esperada — HTTP 200 (OK)

```json
{
  "id": 2,
  "nome": "Maria Oliveira Silva",
  "data_nascimento": "1985-08-22",
  "data_admissao": "2010-07-01"
}
```

---

### 5.2 Atualizar apenas a data de admissão

```bash
curl -X PATCH http://localhost:8080/person/2 \
  -H "Content-Type: application/json" \
  -d '{
    "data_admissao": "2012-10-15"
  }'
```

### Resposta esperada — HTTP 200 (OK)

```json
{
  "id": 2,
  "nome": "Maria Oliveira",
  "data_nascimento": "1985-08-22",
  "data_admissao": "2012-10-15"
}
```

---

### 5.3 Atualizar múltiplos campos

```bash
curl -X PATCH http://localhost:8080/person/2 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Oliveira Atualizada",
    "data_admissao": "2013-01-01"
  }'
```

### Resposta esperada — HTTP 200 (OK)

```json
{
  "id": 2,
  "nome": "Maria Oliveira Atualizada",
  "data_nascimento": "1985-08-22",
  "data_admissao": "2013-01-01"
}
```

---

### 5.4 Tentar atualizar pessoa inexistente

```bash
curl -X PATCH http://localhost:8080/person/999 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pessoa Não Existe"
  }'
```

### Resposta esperada — HTTP 404 (Not Found)

```json
{
  "timestamp": "2026-04-01T20:20:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pessoa com ID 999 não encontrada"
}
```

---

### 5.5 Campo inválido

```bash
curl -X PATCH http://localhost:8080/person/2 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": ""
  }'
```

### Resposta esperada — HTTP 400 (Bad Request)

```json
{
  "timestamp": "2026-04-01T20:25:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Nome é obrigatório"
}
```
---

## 6 Buscar pessoa por ID

```bash
GET /person/{id}
```

Retorna uma pessoa específica com base no ID informado.

Caso o ID não exista, a API retorna **HTTP 404 (Not Found)**.

---

### 6.1 Buscar pessoa existente

```bash
curl -X GET http://localhost:8080/person/1
```

### Resposta esperada — HTTP 200 (OK)

```json
{
  "id": 1,
  "nome": "João Silva",
  "dataNascimento": "1990-05-15",
  "dataAdmissao": "2015-03-10"
}
```

---

### 6.2 Buscar pessoa inexistente

```bash
curl -X GET http://localhost:8080/person/999
```

### Resposta esperada — HTTP 404 (Not Found)

```json
{
  "timestamp": "2026-04-02T09:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pessoa com ID 999 não encontrada"
}
```

---

## 📄 Licença

Este projeto está sob a licença MIT.

Veja o arquivo `LICENSE` para mais detalhes.

---

## 📞 Contato

**Desenvolvedor:** Rafael Barros
**Email:** rafaelbarros.df@gmail.com
**Projeto:** [sccon-geospatial](https://github.com/rafaellbarros/sccon-geospatial)
