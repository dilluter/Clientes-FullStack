# 💼 Sistema de Gestão de Clientes e Serviços (Full-Stack)

Bem-vindo ao repositório do **Clientes-FullStack**! Este é um sistema web completo (Front-End e Back-End) desenvolvido para facilitar o gerenciamento de clientes e o controle de serviços prestados a eles.

O objetivo do sistema é fornecer uma interface intuitiva para cadastrar clientes, registrar serviços vinculados a esses clientes, definir valores e pesquisar o histórico de serviços por filtros específicos (nome e mês).

---

## 🎯 Funcionalidades

- **Gestão de Clientes:** Cadastro, listagem, edição e exclusão de clientes (CRUD completo).
- **Registro de Serviços:** Lançamento de serviços prestados, vinculando ao cliente, com descrição, valor e data.
- **Filtros e Buscas:** Consulta de serviços prestados filtrando por nome do cliente e mês de realização.
- **Validações:** O sistema conta com validações robustas tanto no Front-End quanto no Back-End (ex: CPF válido, campos obrigatórios).

---

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído separando as responsabilidades entre uma API RESTful e uma Single Page Application (SPA).

### 🖥️ Front-End (SPA)
- **Angular:** 9.1.1
- **Node.js:** 16.20.2
- **NPM:** Gerenciador de pacotes
- **HTML5 / CSS3 / TypeScript**
- **Bootstrap** (para estilização e design responsivo)

### ⚙️ Back-End (API REST)
- **Java:** JDK 21
- **Spring Boot:** 3.3.3
- **Spring Data JPA:** Para persistência de dados
- **Banco de Dados:** H2 Database (em memória, ideal para desenvolvimento)
- **SpringDoc OpenAPI (Swagger):** Para documentação da API
- **Lombok:** Produtividade e código limpo
- **Maven:** Gerenciador de dependências

---

## 📋 Pré-requisitos

Para rodar este projeto na sua máquina, você precisará ter instalado:

1. **Java JDK 21**
2. **Maven 3.8+**
3. **Node.js 16.20.2**
4. **Angular CLI 9.1.1** (Instale globalmente usando: `npm install -g @angular/cli@9.1.1`)

---

## 🚀 Como Executar o Projeto

O projeto é dividido em duas pastas principais: o back-end (API) e o front-end (App). Primeiro, você deve clonar o repositório e, em seguida, usar **dois terminais** abertos para rodar ambos simultaneamente.

### Passo 1: Clonar o Repositório

Abra o seu terminal e execute:
> git clone https://github.com/dilluter/Clientes-FullStack.git

Após o clone, entre na pasta principal do projeto:
> cd Clientes-FullStack

### Passo 2: Rodando o Back-End (API)

1. No terminal, acesse a pasta do back-end:
> cd clientes

2. Instale as dependências e compile o projeto:
> mvn clean install

3. Inicie o servidor Spring Boot:
> mvn spring-boot:run

A API estará rodando em: **http://localhost:8080**
Você pode acessar a documentação do Swagger em: **http://localhost:8080/swagger-ui/index.html**

### Passo 3: Rodando o Front-End (Interface)

1. Abra um **novo** terminal (mantenha o do back-end rodando) e acesse a pasta do front-end a partir da raiz do repositório:
> cd clientes-app

2. Instale as dependências do Node:
> npm install

3. Inicie o servidor de desenvolvimento do Angular:
> npm run start

A aplicação Front-End estará disponível no seu navegador em: **http://localhost:4200**

---

## 📂 Estrutura do Repositório

```text
/Clientes-FullStack
├── clientes/                 # Código fonte do Back-End (Spring Boot / Java)
│   ├── src/main/java/        # Controladores, Entidades, Repositórios, Configurações
│   ├── src/main/resources/   # Configurações do banco (application.properties)
│   └── pom.xml               # Dependências do Maven
│
└── clientes-app/             # Código fonte do Front-End (Angular / TypeScript)
    ├── src/app/              # Componentes, Serviços, Módulos
    ├── package.json          # Dependências do NPM
    └── angular.json          # Configurações do Angular Workspace