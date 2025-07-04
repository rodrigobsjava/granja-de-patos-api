#!/bin/bash

# Parar e remover containers e volumes
echo "Parando containers e removendo volumes..."
docker-compose down -v

# Subir o banco PostgreSQL
echo "Subindo o PostgreSQL..."
docker-compose up -d

# Verificar versão do PostgreSQL
echo "Verificando versão do PostgreSQL..."
docker exec -it granja-patos-db psql -U postgres -c "SELECT version();"

# Rodar a aplicação Spring Boot
echo "Rodando a aplicação Spring Boot..."
./mvnw spring-boot:run
