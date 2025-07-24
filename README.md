# Granja de Patos - API REST

Projeto desenvolvido como desafio backend, com foco em boas práticas de programação Java, regras de negócio e geração de relatórios.  
A aplicação simula o controle de vendas de uma granja de patos.

## Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- Spring Data JPA  
- PostgreSQL  
- Flyway (migrações de banco)  
- Apache POI (relatórios em Excel)  
- Lombok  
- JUnit e Mockito (testes)  
- Docker e Docker Compose

## Estrutura do Projeto

- `controller` – Rotas da API
- `service` – Regras de negócio
- `repository` – Acesso ao banco
- `dto` – Entrada e saída de dados
- `model` – Entidades do banco de dados
- `exception` – Tratamento de erros

## Como Rodar Localmente

### Pré-requisitos
Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

- Java 17+
- Maven 3.8+
- Docker
- Docker Compose
- Git
- Postman (opcional, para testes de API)
- IDE de sua preferência (IntelliJ, Eclipse, etc.)
- (Linux/macOS/WSL recomendado para usar o script up-docker-springboot.sh)

### Clonar o Repositório
```
bash
	
git clone https://github.com/rodrigobsjava/granja-de-patos-api.git
cd granja-de-patos-api

```
### Subir o Banco de Dados e a Aplicação
Para facilitar a execução local da aplicação, foi criado o script up-docker-springboot.sh. Ele realiza automaticamente os seguintes passos:
1. Detecta o sistema operacional (Linux, macOS ou Windows)
2. Sobe o container do PostgreSQL via Docker com o Docker Compose
3. Verifica a versão do PostgreSQL para garantir que está rodando corretamente
4. Inicia a aplicação Spring Boot com o Maven Wrapper (mvnw ou mvnw.cmd)

#### Rodar o script

No terminal (Linux/macOS/Windows via WSL ou Git Bash), execute:
```
bash
chmod +x up-docker-springboot.sh
./up-docker-springboot.sh 
```
Esse comando irá:
  * Subir o banco de dados PostgreSQL (porta 5433)

  * Rodar a aplicação Spring Boot (porta 8082)

> Caso não queira usar o script, você pode seguir os passos abaixo manualmente:
```
bash

docker-compose up --build
```

### Acessar no Navegador ou Postman

  * API rodando em: http://localhost:8082

  * Banco de Dados acessível via:

      * Host: localhost
      * Porta: 5433
      * Usuário: postgres
      * Senha: 12345678

  >  As tabelas e dados serão criados automaticamente via Flyway.

---

## Funcionalidades Implementadas

### Patos
- Cadastro de patos
- Marcar pato como vendido automaticamente após venda

### Vendedores
- Cadastro e listagem
- Validação de CPF único
- Impede exclusão se houver vendas

### Clientes
- Cadastro e listagem
- Verifica se o cliente tem desconto (20%) após 3 compras

### Vendas
- Cadastro de venda (com múltiplos patos)
- Cálculo automático do valor total com desconto (se aplicável)

### Relatórios

#### Relatório de Vendas em Excel
- Exporta para `.xlsx` com:
  - Cliente
  - Vendedor
  - Valor total
  - Data
  - Quantidade de patos

Acesse via:  
`GET /relatorios/vendas/excel`

#### Ranking de Vendedores
- Lista os vendedores ordenados por total vendido e número de vendas
+ Retorna os vendedores ordenados por total vendido e número de vendas, incluindo:
  - Nome do vendedor
  - Quantidade de vendas
  - Total em reais vendido

Acesse via:  
`GET /relatorios/ranking-vendedores`

---

## 📬 Exemplos de Requisições (Postman)

Você pode importar o JSON com os endpoints no Postman ou usar os seguintes:

### Vendedores
- `POST /vendedores` - Cadastrar vendedor
- `GET /vendedores` - Listar vendedores

### Clientes
- `POST /clientes` - Cadastrar cliente
- `GET /clientes` - Listar clientes

### Patos
- `POST /patos` - Cadastrar pato
- `GET /patos` - Listar patos
- `GET /patos/disponiveis` - Listar patos disponíveis para venda
- `DELETE /patos/{id}` - Excluir pato (não pode se houver vendas)

### Vendas
- `POST /vendas` - Registrar nova venda
- `GET /vendas` - Listar vendas
- `GET /vendas/{id}` - Detalhes da venda por ID

### Relatórios
- `GET /relatorios/vendas/excel` - Exportar relatório de vendas em Excel
- `GET /relatorios/ranking-vendedores` - Exibir ranking de vendedores

---
## Exemplo de Simulação de Venda (via Postman ou API)
Este é um fluxo completo de criação de uma venda com desconto aplicado (caso o cliente tenha 3 ou mais compras).
  #### 1. Criar um Vendedor
  `POST http://localhost:8082/vendedores`
  ```
  json
  {
    "nome": "João da Silva",
    "cpf": "123.456.789-00",
    "matricula": "MAT1234"
  }
  ```
---
  #### 2. Criar um Cliente
  `POST http://localhost:8082/clientes`
  ```
  json
  {
    "nome": "Maria Oliveira",
    "cpf": "987.654.321-00"
  }
  ```
---
  #### 3. Criar Patos
  `POST http://localhost:8082/patos`
  ```
  json
  {
    "nome": "Pato 1",
    "descricao": "Pato saudável",
    "preco": 100.0
  }
  ```
  ```
  json
  {
    "nome": "Pato 2",
    "descricao": "Pato gordo",
    "preco": 150.0
  }
  ```
---
  #### 4. Fazer a Venda
  `POST http://localhost:8082/vendas`
  ```
  json
  {
    "clienteId": "UUID_DO_CLIENTE",
    "vendedorId": "UUID_DO_VENDEDOR",
    "patoIds": [
      "UUID_PATO_1",
      "UUID_PATO_2"
    ]
  }
  ```
  Substitua os UUID_* pelos valores retornados nas respostas anteriores. O sistema irá:

  * Calcular automaticamente o valor total da venda.
  * Aplicar 20% de desconto caso o cliente já tenha feito 3 ou mais compras.
  * Marcar os patos como vendidos.
---
  #### 5. Consultar as Vendas
  `GET http://localhost:8082/vendas`

---

  #### 6. Exportar Relatório em Excel
  `GET http://localhost:8082/relatorios/vendas/excel`

---


## Testes Automatizados

- Testes unitários e de integração para controllers e services

- Validação das regras de negócio, como:

  - CPF único para vendedores
  - Impedir venda de pato já vendido
  - Aplicação de desconto para clientes com 3 ou mais compras

- Cobertura básica para garantir funcionamento esperado

---

## Sobre o Desafio

Este projeto foi desenvolvido como parte do Desafio Preço Justo, proposto no processo seletivo para a vaga de Desenvolvedor Back End.

O desafio consistia em construir uma API baseada nos requisitos descritos no documento fornecido, incluindo:

  * Cadastro e gerenciamento de vendedores, clientes, patos e vendas
  * Regras de negócio específicas
  * Relatórios em Excel
  * Validações e testes automatizados

---

## Autor

Desenvolvido por [Rodrigo Barbosa De Sousa](https://www.linkedin.com/in/rodrigobsjava/)  
Email - rodrigobarbosadesousa5608@gmail.com
GitHub - [rodrigobsjava](https://github.com/rodrigobsjava)

---

## Licença

Este projeto foi desenvolvido exclusivamente para fins de avaliação técnica no processo seletivo da empresa Preço Justo.

Seu uso é restrito ao propósito de análise técnica e não deve ser utilizado para fins comerciais ou de distribuição sem autorização.
