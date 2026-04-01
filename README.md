# SCCON Geospatial - API de Pessoas

## 📋 Sobre o Projeto

API REST desenvolvida com Spring Boot 3.5.13 para gerenciamento de pessoas, utilizando armazenamento em memória (Map) para alta performance nas operações de busca por ID.

### 🚀 Funcionalidades Implementadas

- ✅ **Modelo de Pessoa**: Entidade com atributos (id, nome, dataNascimento, dataAdmissão)
- ✅ **Armazenamento em Memória**: Mapa ConcurrentHashMap otimizado para busca por ID (O(1))
- ✅ **Inicialização Automática**: População inicial com 3 pessoas no momento da inicialização
- ✅ **Endpoint GET /person**: Retorna lista ordenada alfabeticamente por nome
- ✅ **Endpoint POST /person**: Inclui nova pessoa no mapa com regras de negócio específicas
    - ID não especificado → atribui automaticamente o maior ID + 1
    - ID especificado e já existente → retorna HTTP 409 (Conflict)
    - Validação de campos obrigatórios

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.5.13
- Spring Web
- Maven

## 🔧 Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- Git (opcional)

## 🚀 Como Executar a Aplicação

### 1. Clone o repositório (ou baixe os arquivos)

```bash
git clone https://github.com/rafaellbarros/sccon-geospatial.git
cd sccon-geospatial
```

### 2. Execute a aplicação

#### Opção 1: Usando Maven

```bash
mvn clean install
```

```bash
mvn spring-boot:run
```

#### Opção 2: Gerando JAR e executando

```bash
mvn clean package
```

```bash

java -jar target/sccon-geospatial-0.0.1-SNAPSHOT.jar
```

### 3. Endpoints Disponíveis

#### Base URL

```bash
http://localhost:8080/person
```

#### 1. Listar todas as pessoas (ordenadas por nome - A-Z)
```bash
GET /person
```
#### 2. Criar pessoas
```bash
POST /person
```

#### 2.1 Criar pessoa sem ID (ID automático)

Quando o campo `id` não é informado, a API atribui automaticamente o próximo ID disponível.

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Mariana Costa",
    "dataNascimento": "1993-09-18",
    "dataAdmissao": "2019-04-22"
  }'
```

#### Resposta esperada — HTTP 201 (Created)

```json
{
  "id": 4,
  "nome": "Mariana Costa",
  "dataNascimento": "1993-09-18",
  "dataAdmissao": "2019-04-22"
}
```

---

#### 2.2 Criar pessoa com ID informado e disponível

Quando o `id` é informado e ainda não existe no mapa, a pessoa é criada com sucesso.

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "id": 10,
    "nome": "Fernanda Souza",
    "dataNascimento": "1991-07-30",
    "dataAdmissao": "2017-11-15"
  }'
```

#### Resposta esperada — HTTP 201 (Created)

```json
{
  "id": 10,
  "nome": "Fernanda Souza",
  "dataNascimento": "1991-07-30",
  "dataAdmissao": "2017-11-15"
}
```

---

#### 2.3 Tentar criar pessoa com ID já existente (Conflito)

Quando o `id` já existir, a API retorna erro `409 Conflict`.

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nome": "Conflito Teste",
    "dataNascimento": "1990-01-01",
    "dataAdmissao": "2020-01-01"
  }'
```

#### Resposta esperada — HTTP 409 (Conflict)

```json
{
  "timestamp": "2026-04-01T19:17:46",
  "status": 409,
  "error": "Conflict",
  "message": "Já existe uma pessoa com o ID 1. Nome: João Silva"
}
```

---

#### 2.4 Tentar criar pessoa com campos obrigatórios ausentes

Quando algum campo obrigatório não for informado, a API retorna erro `400 Bad Request`.

```bash
curl -X POST http://localhost:8080/person \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "",
    "dataNascimento": null,
    "dataAdmissao": null
  }'
```

#### Resposta esperada — HTTP 400 (Bad Request)

```json
{
  "timestamp": "2026-04-01T19:20:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Nome é obrigatório"
}
```

> Obs.: A mensagem pode variar conforme o campo inválido.

---

#### 2.5 Verificar lista ordenada após inserções

A listagem retorna as pessoas ordenadas alfabeticamente por nome.

```bash
curl -X GET http://localhost:8080/person
```

#### Resposta esperada — HTTP 200 (OK)

```json
[
  {
    "id": 3,
    "nome": "Carlos Santos",
    "dataNascimento": "1995-12-03",
    "dataAdmissao": "2018-01-15"
  },
  {
    "id": 10,
    "nome": "Fernanda Souza",
    "dataNascimento": "1991-07-30",
    "dataAdmissao": "2017-11-15"
  },
  {
    "id": 1,
    "nome": "João Silva",
    "dataNascimento": "1990-05-15",
    "dataAdmissao": "2015-03-10"
  },
  {
    "id": 4,
    "nome": "Mariana Costa",
    "dataNascimento": "1993-09-18",
    "dataAdmissao": "2019-04-22"
  },
  {
    "id": 2,
    "nome": "Maria Oliveira",
    "dataNascimento": "1985-08-22",
    "dataAdmissao": "2010-07-01"
  }
]
```

---

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

📞 Contato
Desenvolvedor: Rafael Barros

Email: rafaelbarros.df@gmail.com

Projeto: [sccon-geospatial](https://github.com/rafaellbarros/sccon-geospatial)
