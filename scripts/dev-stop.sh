#!/bin/bash

###############################################################################
# Script: dev-stop.sh
# Descripción: Detiene todos los servicios de desarrollo de ElProducto
# Uso: ./scripts/dev-stop.sh
###############################################################################

set -e  # Exit on error

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}ElProducto - Detener Desarrollo${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}Error: Debe ejecutar este script desde la raíz del proyecto${NC}"
    exit 1
fi

echo "→ Deteniendo servicios..."
docker-compose down

echo ""
echo -e "${GREEN}Servicios detenidos correctamente${NC}"
echo ""
echo -e "${YELLOW}Nota: Los datos de PostgreSQL se mantienen en volumes${NC}"
echo "Para eliminar los datos también, usa: ./scripts/dev-reset.sh"
echo ""