# Clientes API (Back-End) 🚀

Este é o back-end do sistema **Clientes-FullStack**, uma API RESTful robusta desenvolvida para gerenciar o cadastro de clientes e o registro detalhado de serviços prestados. A aplicação utiliza tecnologias modernas para garantir performance, segurança e facilidade de manutenção.

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

- **Java JDK 21**
- **Maven 3.8+**
- Uma IDE (IntelliJ IDEA, Eclipse ou VS Code)

---

## 🛠️ Tecnologias e Frameworks

O projeto foi construído utilizando o melhor do ecossistema Java:

- **Java 21**: Aproveitando as últimas melhorias da linguagem.
- **Spring Boot 3.3.3**: Framework principal para agilizar o desenvolvimento.
- **Spring Data JPA**: Abstração para persistência de dados e consultas facilitadas.
- **H2 Database**: Banco de dados em memória, ideal para desenvolvimento e testes rápidos.
- **SpringDoc OpenAPI (Swagger)**: Geração automática de documentação e interface de testes.
- **Lombok**: Para um código mais limpo, eliminando getters, setters e construtores manuais.
- **Bean Validation**: Validação rigorosa dos dados de entrada (como CPF e nomes).
- **JUnit & Mockito**: Garantia de qualidade com testes automatizados de unidade e integração.

---

## 🚀 Como Executar a Aplicação

Siga os passos abaixo na pasta raiz do projeto back-end:

1. **Instalar dependências e compilar o projeto:**
> mvn clean install

2. **Rodar a aplicação:**
> mvn spring-boot:run

A API estará rodando em: http://localhost:8080

---

## 📖 Documentação da API (Swagger)

A API possui documentação interativa completa. Para visualizar todos os endpoints disponíveis, verificar os parâmetros de requisição e testar as rotas em tempo real, acesse o painel do Swagger diretamente pelo seu navegador:

🔗 **Acesse o Swagger:** http://localhost:8080/swagger-ui/index.html

---

## 📂 Estrutura de Pacotes

- `config`: Configurações globais (CORS, Internacionalização).
- `controller`: Porta de entrada da API (Endpoints REST).
- `dto`: Objetos de transferência para requisições seguras.
- `exception`: Central de tratamento de erros e mensagens de validação.
- `model.entity`: Mapeamento das tabelas do banco de dados.
- `model.repository`: Interfaces para operações CRUD.
- `util`: Classes de suporte (como conversores de tipos).

---

## 🧪 Testes Automatizados

Para rodar a bateria de testes e garantir que tudo está funcionando corretamente:
> mvn test

---

## 📊 Informações Técnicas

- **Porta Padrão:** 8080
- **Versão Java:** 21
- **Banco de Dados:** H2 (In-memory)

---
