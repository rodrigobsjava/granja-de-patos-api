#!/bin/bash

# Detectar sistema operacional
OS=$(uname)

echo "Detectando sistema operacional: $OS"

# Subir o banco PostgreSQL
echo "Subindo o PostgreSQL..."
docker-compose up -d

# Verificar versão do PostgreSQL
echo "Verificando versão do PostgreSQL..."
docker exec granja-patos-db psql -U postgres -c "SELECT version();"

# Executar o Maven Wrapper correto
if [[ "$OS" == "MINGW"* ]] || [[ "$OS" == "CYGWIN"* ]] || [[ "$OS" == "MSYS"* ]]; then
  echo "Rodando a aplicação Spring Boot no Windows..."
  ./mvnw.cmd spring-boot:run
else
  echo "Rodando a aplicação Spring Boot no Linux/macOS..."
  ./mvnw spring-boot:run
fi
