# 🛒 E-commerce Hardware - Backend

> API REST robusta para e-commerce de hardware desenvolvida com Spring Boot

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-red?logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📋 Sobre o Projeto

Este é o backend de um e-commerce especializado em hardware, desenvolvido com Spring Boot. A API fornece endpoints RESTful completos para gerenciamento de produtos, usuários, pedidos, autenticação e integração com serviços externos.

> 🔗 **Frontend**: Este backend é consumido pelo [Frontend Angular](../frontend/README.md). Veja as instruções de instalação do frontend após configurar o backend.

## ✨ Principais Funcionalidades

- 🔐 **Autenticação e Autorização** - JWT + OAuth2 (Google)
- 👤 **Gestão de Usuários** - CRUD completo com validações
- 📦 **Catálogo de Produtos** - Gerenciamento de produtos de hardware
- 🛒 **Carrinho de Compras** - Sessões de carrinho persistentes
- 💰 **Pedidos** - Processamento completo de pedidos
- 💳 **Pagamentos** - Integração com AbacatePay
- 📍 **CEP e Endereços** - Consulta via ViaCEP
- 🗺️ **Cálculo de Frete** - Google Matrix Distance API
- 📧 **Notificações** - Envio de emails transacionais
- 📊 **Auditoria** - Spring Data Envers para histórico de alterações
- 📝 **Documentação** - Swagger/OpenAPI automático

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 3.3.4** - Framework principal
- **Spring Web** - API REST
- **Spring WebFlux** - Cliente HTTP reativo
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **Spring OAuth2 Client** - Integração OAuth2

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **Spring Data Envers** - Auditoria e versionamento

### Segurança
- **JWT (Java-JWT 4.4.0)** - Tokens de autenticação
- **Spring Security** - Framework de segurança
- **OAuth2** - Login social (Google)

### Utilitários
- **Lombok** - Redução de código boilerplate
- **Bean Validation** - Validação de dados
- **Commons Text 1.10.0** - Manipulação de strings
- **Jackson 2.15.2** - Serialização JSON
- **Spring Mail** - Envio de emails

### Documentação e Testes
- **SpringDoc OpenAPI 2.6.0** - Documentação Swagger
- **Spring Boot Test** - Testes unitários e integração
- **Spring Security Test** - Testes de segurança

### Ferramentas de Desenvolvimento
- **Spring Boot DevTools** - Hot reload
- **Maven** - Gerenciamento de dependências

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java JDK 21](https://openjdk.org/) ou superior
- [Maven 3.9+](https://maven.apache.org/)
- [PostgreSQL 14+](https://www.postgresql.org/)
- [Git](https://git-scm.com/)

```bash
# Verificar versões instaladas
java -version
mvn -version
psql --version
```

## 🗄️ Configuração do Banco de Dados

### 1. Instalar PostgreSQL

#### Windows
Baixe o instalador em [postgresql.org](https://www.postgresql.org/download/windows/)

## 🚀 Instalação e Configuração

### 1️⃣ Clone o Repositório

```bash
git clone https://github.com/seu-usuario/loopz.git
```

### 2️⃣ Configure as Variáveis de Ambiente

Crie um arquivo `application.properties` em `src/main/resources/`:

```properties
# ==========================================
# CONFIGURAÇÕES DO SERVIDOR
# ==========================================
server.port=8085
spring.application.name=ecommerce-hardware

# ==========================================
# BANCO DE DADOS
# ==========================================
spring.datasource.url=jdbc:postgresql://localhost:5432/loopz
spring.datasource.username=ecommerce_user
spring.datasource.password=senha_segura
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# ==========================================
# SPRING DATA ENVERS (Auditoria)
# ==========================================
spring.jpa.properties.org.hibernate.envers.audit_table_suffix=_AUD
spring.jpa.properties.org.hibernate.envers.revision_field_name=REV
spring.jpa.properties.org.hibernate.envers.revision_type_field_name=REVTYPE

# ==========================================
# SEGURANÇA - JWT
# ==========================================
jwt.secret=seu_jwt_secret_super_seguro_com_no_minimo_256_bits
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# ==========================================
# OAUTH2 - GOOGLE
# ==========================================
spring.security.oauth2.client.registration.google.client-id=SEU_GOOGLE_CLIENT_ID.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=SEU_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/api/auth/google/callback
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/v2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo

# ==========================================
# GOOGLE MAPS API
# ==========================================
google.maps.api.key=SUA_GOOGLE_MAPS_API_KEY

# ==========================================
# ABACATE PAY
# ==========================================
abacatepay.api.key=SUA_ABACATE_PAY_API_KEY
abacatepay.secret=SEU_ABACATE_PAY_SECRET
abacatepay.webhook.secret=SEU_WEBHOOK_SECRET

# ==========================================
# VIACEP API
# ==========================================
viacep.api.url=https://viacep.com.br/ws

# ==========================================
# EMAIL
# ==========================================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu.email@gmail.com
spring.mail.password=sua_senha_de_app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# ==========================================
# CORS
# ==========================================
cors.allowed.origins=http://localhost:4200,http://localhost:3000

# ==========================================
# SWAGGER/OPENAPI
# ==========================================
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# ==========================================
# LOGS
# ==========================================
logging.level.dev.trier.ecommerce=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### 3️⃣ Instale as Dependências

```bash
mvn clean install
```

### 4️⃣ Execute a Aplicação

#### Modo Desenvolvimento

```bash
mvn spring-boot:run
```

#### Compilar e Executar JAR

```bash
mvn clean package
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

#### Com Perfil Específico

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

A aplicação estará rodando em **http://localhost:8080**

## 🔑 Obtendo as Chaves de API

### 1. JWT Secret

Gere uma chave segura de 256 bits:

```bash
# Linux/Mac
openssl rand -base64 64

# Ou use um gerador online
https://generate-random.org/api-token-generator
```

### 2. OAuth2 Google

1. Acesse o [Google Cloud Console](https://console.cloud.google.com/)
2. Crie um novo projeto
3. Ative a **Google+ API**
4. Vá em **Credenciais** → **Criar credenciais** → **ID do cliente OAuth 2.0**
5. Configure:
   - Tipo: Aplicativo da Web
   - URIs de redirecionamento autorizados:
     - `http://localhost:8085/api/auth/google/callback`
     - `https://seu-dominio.com/api/auth/google/callback`
6. Copie **Client ID** e **Client Secret**

### 3. Google Maps API

1. No mesmo projeto do Google Cloud
2. Ative a **Distance Matrix API**
3. Vá em **Credenciais** → **Criar credenciais** → **Chave de API**
4. Configure restrições (recomendado)

### 4. AbacatePay

1. Cadastre-se em [AbacatePay](https://abacatepay.com/)
2. Acesse o painel
3. Copie a **API Key** e o **Secret**
4. Configure o webhook: `https://seu-dominio.com/api/payments/webhook`

**Documentação**: [AbacatePay Docs](https://docs.abacatepay.com/pages/introduction)

### 5. Email (Gmail)

1. Ative a verificação em 2 etapas na sua conta Google
2. Gere uma senha de app em [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
3. Use essa senha no `application.properties`

## 📚 Documentação da API

### Swagger UI

Após iniciar a aplicação, acesse para conhecimento dos endpoints:

```
http://localhost:8085/swagger-ui.html
```

Todas as rotas protegidas requerem token JWT no header:

```http
Authorization: Bearer SEU_TOKEN_JWT_AQUI
```

### Exemplo de Requisição

```bash
curl -X GET http://localhost:8085/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## 🔒 Segurança

### Configuração de CORS

O backend está configurado para aceitar requisições do frontend:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "https://seu-dominio.com"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        // ...
    }
}
```

### Hierarquia de Roles

- **USER**: Usuário padrão
- **ADMIN**: Administrador do sistema

## 🐛 Problemas Comuns

### ❌ Erro de Conexão com Banco

```bash
# Verificar se o PostgreSQL está rodando
sudo systemctl status postgresql

# Testar conexão
psql -h localhost -U ecommerce_user -d ecommerce_hardware
```

### ❌ Porta 8080 em Uso

Altere em `application.properties`:

```properties
server.port=8081
```

### ❌ Erro com JWT Secret

Certifique-se de que o secret tem pelo menos 256 bits (32 caracteres base64)

### ❌ Erro com OAuth2

Verifique:
- Client ID e Secret corretos
- URIs de redirecionamento configuradas no Google Cloud
- Aplicação rodando na porta correta

## 📊 Monitoramento

### Actuator Endpoints

```http
GET /actuator/health        # Status da aplicação
GET /actuator/info          # Informações da aplicação
GET /actuator/metrics       # Métricas
```

### Padrões de Código

- Siga as convenções do Java
- Use Lombok para reduzir boilerplate
- Escreva testes para novas features
- Documente métodos públicos com Javadoc

## 📖 Documentação Adicional

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [Spring Security](https://docs.spring.io/spring-security/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/)
- [AbacatePay API](https://docs.abacatepay.com/)
- [Frontend Angular](../frontend/README.md)


## 🙏 Agradecimentos

- Spring Community
- AbacatePay Team
- PostgreSQL Community

---

🔗 **Links Relacionados**:
- [🎨 Frontend Angular](../frontend/README.md)
- [📖 Swagger UI](http://localhost:8085/swagger-ui.html)

⭐ Se este projeto foi útil, considere dar uma estrela!
