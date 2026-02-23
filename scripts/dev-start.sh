#!/bin/bash

###############################################################################
# Script: dev-start.sh
# Descripción: Inicia el entorno de desarrollo completo de ElProducto
# Uso: ./scripts/dev-start.sh
###############################################################################

set -e  # Exit on error

# Colors para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}ElProducto - Inicio de Desarrollo${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}Error: Debe ejecutar este script desde la raíz del proyecto${NC}"
    exit 1
fi

# Verificar que Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker no está corriendo${NC}"
    exit 1
fi

# Verificar que existe .env
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}Advertencia: No existe archivo .env${NC}"
    echo -e "${YELLOW}Copiando .env.example a .env...${NC}"
    cp .env.example .env
    echo -e "${YELLOW}Por favor, edita .env y configura tus variables${NC}"
    echo ""
fi

echo -e "${GREEN}Iniciando servicios de desarrollo...${NC}"
echo ""

# Iniciar PostgreSQL para desarrollo
echo "→ Iniciando PostgreSQL..."
docker-compose up -d postgres

echo ""
echo -e "${GREEN}Esperando a que los servicios estén listos...${NC}"
sleep 5

# Verificar health de PostgreSQL
echo "→ Verificando PostgreSQL..."
if docker exec elproducto-postgres pg_isready -U postgres > /dev/null 2>&1; then
    echo -e "${GREEN}✓ PostgreSQL está listo${NC}"
else
    echo -e "${RED}✗ PostgreSQL no está listo${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Entorno de desarrollo iniciado!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Servicios disponibles:"
echo "  - PostgreSQL: localhost:5432"
echo ""
echo "Para desarrollar los microservicios:"
echo "  cd backend/data-collector-service && ./mvnw spring-boot:run"
echo ""
echo "Para ver logs:"
echo "  docker-compose logs -f postgres"
echo ""
echo "Para detener:"
echo "  ./scripts/dev-stop.sh"
echo ""