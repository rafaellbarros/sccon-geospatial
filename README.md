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

##### 1. Listar todas as pessoas (ordenadas por nome - A-Z)
```bash
GET /person
```
---

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

📞 Contato
Desenvolvedor: Rafael Barros

Email: rafaelbarros.df@gmail.com

Projeto: [sccon-geospatial](https://github.com/rafaellbarros/sccon-geospatial)
