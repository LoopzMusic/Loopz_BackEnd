# 🛒 Projeto Spring Boot — Ecommerce de Hardware

Este é um projeto desenvolvido com **Spring Boot 3.3.4**, utilizando as tecnologias mais recentes do ecossistema **Java 21** para construção de aplicações web seguras, escaláveis e modernas.

---

## 🚀 Tecnologias Utilizadas

- **Java**: 21
- **Spring Boot**: 3.3.4
- **Spring Web** → criação de APIs RESTful
- **Spring Data JPA** → integração com banco de dados relacional
- **Spring Validation** → validação de dados de entrada
- **Spring Security** → autenticação e autorização
- **Spring Mail** → envio de e-mails
- **PostgreSQL 17** → banco de dados relacional
- **Swagger / Springdoc OpenAPI** → documentação e testes das APIs
- **Lombok** → geração automática de getters, setters e construtores
- **DevTools** → recarga automática durante o desenvolvimento
- **Apache Commons Text** → manipulação e sanitização de strings
- **Auth0 Java JWT** → autenticação baseada em tokens JWT
- **JUnit + Spring Security Test** → testes automatizados de integração e segurança

---

## ⚙️ Funcionalidades Principais

- CRUD completo para entidades do sistema (usuários, produtos, pedidos, etc.).
- APIs RESTful seguindo boas práticas REST.
- Validação de dados com **Bean Validation** (`@NotNull`, `@Email`, `@Size`, etc.).
- Autenticação JWT com **Spring Security** e **Auth0 Java JWT**.
- Configuração de segurança com **roles** e permissões.
- Envio de e-mails automáticos via **Spring Mail**.
- Integração com **PostgreSQL 17**.
- Documentação e testes das APIs com **Swagger UI**.
- Recarga automática no ambiente de desenvolvimento com **DevTools**.


## 🛠️ Requisitos para Execução

- **Java 21** instalado
- **Maven 3.9+**
- **PostgreSQL 17** em execução
- IDE recomendada: **IntelliJ IDEA** ou **VS Code com extensão Spring Boot Tools**

---

## ▶️ Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/sua-organizacao/ecommerce.git
   cd ecommerce


## ▶️ Como Executar o Projeto

1. **Configure o banco de dados no arquivo `application.properties`:**

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
   spring.datasource.username=postgres
   spring.datasource.password=senha
   spring.jpa.hibernate.ddl-auto=update
   spring.mail.username={seuEmail}
   spring.mail.password={Sua senha gerada pelo google na aba "Senhas de APP"}
