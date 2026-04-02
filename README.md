# SCCON Geospatial - API de Pessoas

![Version](https://img.shields.io/badge/version-v1.0-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.13-brightgreen)

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
- ✅ **Endpoint GET /person/{id}/age?output={days|months|years}**
  - Retorna a idade atual da pessoa em dias, meses ou anos completos
  - Caso o ID não exista → HTTP 404 (Not Found)
  - Caso o parâmetro `output` seja inválido → HTTP 400 (Bad Request)
- ✅ **Endpoint GET /person/{id}/salary?output={min|full}**
  - Retorna o salário atual da pessoa em valor monetário (`full`) ou em quantidade de salários mínimos (`min`)
  - Cálculo baseado no salário inicial de **R$ 1550,00**
  - Acréscimo de **55,14% ao ano completo de empresa**
  - Saída com **duas casas decimais e arredondamento para cima**
  - Caso o ID não exista → HTTP 404 (Not Found)
  - Caso o parâmetro `output` seja inválido → HTTP 400 (Bad Request)

Nota: Observou-se uma divergência de R$ 0,02 no valor total (full) em relação ao exemplo do enunciado. A implementação seguiu estritamente o cálculo de 55,14% sobre o salário base e arredondamento UP, priorizando a precisão do BigDecimal. O valor 18% no código não gera o resultado conforme o requisto.

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

#### Opção 1: 🐳 Executando com Docker

```bash
mvn clean package
docker compose up --build
```

API disponível em:

```bash
http://localhost:8080/person
```

## 📬 Collection para Postman / Insomnia

O projeto disponibiliza uma collection pronta para facilitar a execução e validação dos endpoints da API.

Arquivo disponível na raiz do projeto:

```bash
sccon-geospatial-api-collection.json
```

Esta collection pode ser importada tanto no **Postman** quanto no **Insomnia**.

Ela contém exemplos prontos para todos os endpoints implementados:

- `GET /person`
- `GET /person/{id}`
- `POST /person`
- `PUT /person/{id}`
- `PATCH /person/{id}`
- `DELETE /person/{id}`
- `GET /person/{id}/age?output={days|months|years}`
- `GET /person/{id}/salary?output={min|full}`

---

### 📥 Como importar no Postman

1. Abra o **Postman**
2. Clique em **Import**
3. Selecione o arquivo:

```bash
sccon-geospatial-api-collection.json
```

4. Clique em **Import**

A collection ficará disponível na área lateral para execução dos testes.

---

### 📥 Como importar no Insomnia

1. Abra o **Insomnia**
2. Clique em **Create**
3. Selecione **Import/Export**
4. Escolha **Import Data**
5. Selecione o arquivo:

```bash
sccon-geospatial-api-collection.json
```

6. Confirme a importação

---

### 🚀 Execução rápida dos testes

Após subir a aplicação localmente ou via Docker:

```bash
http://localhost:8080/person
```

Basta executar as requisições diretamente pela collection importada.

---

#### Opção 2: Usando Maven

```bash
mvn clean install
mvn spring-boot:run
```

#### Opção 3: Gerando JAR e executando

```bash
mvn clean package
java -jar target/sccon-geospatial-1.0.jar
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

## 7. Consultar idade atual da pessoa

```bash
GET /person/{id}/age?output={days|months|years}
```

Retorna a idade atual da pessoa em **dias**, **meses** ou **anos completos**, de acordo com o parâmetro `output`.

Valores aceitos para `output`:

- `days`
- `months`
- `years`

Caso o ID não exista, a API retorna **HTTP 404 (Not Found)**.

Caso o parâmetro `output` seja inválido, a API retorna **HTTP 400 (Bad Request)**.

---

### 7.1 Consultar idade em dias

```bash
curl -X GET "http://localhost:8080/person/1/age?output=days"
```

### Resposta esperada — HTTP 200 (OK)

```json
8342
```

---

### 7.2 Consultar idade em meses

```bash
curl -X GET "http://localhost:8080/person/1/age?output=months"
```

### Resposta esperada — HTTP 200 (OK)

```json
274
```

---

### 7.3 Consultar idade em anos

```bash
curl -X GET "http://localhost:8080/person/1/age?output=years"
```

### Resposta esperada — HTTP 200 (OK)

```json
22
```

---

### 7.4 Pessoa não encontrada

```bash
curl -X GET "http://localhost:8080/person/999/age?output=years"
```

### Resposta esperada — HTTP 404 (Not Found)

```json
{
  "timestamp": "2026-04-02T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pessoa com ID 999 não encontrada"
}
```

---

### 7.5 Parâmetro output inválido

```bash
curl -X GET "http://localhost:8080/person/1/age?output=invalid"
```

### Resposta esperada — HTTP 400 (Bad Request)

```json
{
  "timestamp": "2026-04-02T10:05:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Formato de saída inválido. Use: days, months ou years"
}
```
---

## 8. Consultar salário atual da pessoa

```bash
GET /person/{id}/salary?output={min|full}
```

Retorna o salário atual da pessoa com base na data de admissão.

### Regras de cálculo

- Salário inicial: **R$ 1550,00**
- Acréscimo anual: **18% do valor base**
- Valor do salário mínimo: **R$ 1302,00**
- Cálculo realizado com base em **anos completos de empresa**
- Resultado com **2 casas decimais**
- Arredondamento: **para cima**

Valores aceitos para `output`:

- `full` → valor total em reais
- `min` → quantidade equivalente em salários mínimos

Caso o ID não exista, a API retorna **HTTP 404 (Not Found)**.

Caso o parâmetro `output` seja inválido, a API retorna **HTTP 400 (Bad Request)**.

---

### 8.1 Consultar salário em reais

```bash
curl -X GET "http://localhost:8080/person/1/salary?output=full"
```

### Resposta esperada — HTTP 200 (OK)

```json
3259.34
```

---

### 8.2 Consultar salário em salários mínimos

```bash
curl -X GET "http://localhost:8080/person/1/salary?output=min"
```

### Resposta esperada — HTTP 200 (OK)

```json
2.51
```

---

### 8.3 Pessoa não encontrada

```bash
curl -X GET "http://localhost:8080/person/999/salary?output=full"
```

### Resposta esperada — HTTP 404 (Not Found)

```json
{
  "timestamp": "2026-04-02T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pessoa com ID 999 não encontrada"
}
```

---

### 8.4 Parâmetro output inválido

```bash
curl -X GET "http://localhost:8080/person/1/salary?output=invalid"
```

### Resposta esperada — HTTP 400 (Bad Request)

```json
{
  "timestamp": "2026-04-02T10:35:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Formato de saída inválido. Use: min ou full"
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
