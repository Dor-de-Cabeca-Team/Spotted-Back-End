# Rede Social Back-End

Este é o repositório do back-end de uma aplicação de rede social desenvolvida em Java com Spring Boot.

## Clonando o Repositório

Para começar, clone este repositório em sua máquina local:

```bash
git clone https://github.com/Dor-de-Cabeca-Team/Rede-Social-Back-End.git
cd Rede-Social-Back-End
```

## Configuração do Banco de Dados

1. Certifique-se de ter o PostgreSQL instalado em sua máquina.
2. Crie um banco de dados com o nome `rede`.

### Configurando o `application.properties`

Abra o arquivo `src/main/resources/application.properties` e configure as propriedades do banco de dados de acordo com o seu ambiente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rede
spring.datasource.username=postgres
spring.datasource.password=root
```

- **spring.datasource.url**: A URL de conexão do banco de dados.
- **spring.datasource.username**: O nome de usuário do banco de dados.
- **spring.datasource.password**: A senha do banco de dados.

> **Nota**: Substitua `postgres` e `root` pelos valores correspondentes ao seu ambiente.

## Executando o Projeto

Após configurar o banco de dados e o `application.properties`, inicie o projeto com o comando:

```bash
./mvnw spring-boot:run
```

ou, se preferir, execute diretamente a classe `main` pelo seu IDE.

## Pronto!

O projeto deve estar rodando em [http://localhost:8080](http://localhost:8080).
