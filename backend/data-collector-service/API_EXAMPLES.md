# API Examples - Data Collector Service

Este documento contiene ejemplos detallados de uso de la API del Data Collector Service.

## Índice

- [Configuración Inicial](#configuración-inicial)
- [Endpoints Disponibles](#endpoints-disponibles)
- [Ejemplos Avanzados](#ejemplos-avanzados)
- [Testing](#testing)

---

## Configuración Inicial

### Variables de Entorno

Puedes configurar las siguientes variables de entorno:

```bash
export API_FOOTBALL_KEY="tu-api-key-aqui"
export SERVER_PORT=8081
```

### Verificar que el servicio está corriendo

```bash
curl http://localhost:8081/actuator/health
```

---

## Endpoints Disponibles

### 1. POST /api/v1/collector/countries

Recolecta todos los países disponibles desde API-Football.

#### Uso básico:

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries \
  -H "Content-Type: application/json"
```

#### Con salida formateada (jq):

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries | jq
```

#### Guardar respuesta en archivo:

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries \
  -o response.json
```

#### Ver solo los códigos de países:

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries | \
  jq '.data[].code'
```

#### Contar países recolectados:

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries | \
  jq '.count'
```

#### Filtrar países específicos (ejemplo: países de Sudamérica):

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries | \
  jq '.data[] | select(.code | IN("AR", "BR", "CL", "CO", "EC", "PE", "UY", "VE"))'
```

---

## Ejemplos Avanzados

### Ejecutar con timeout personalizado

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries \
  --max-time 60
```

### Ejecutar con verbose output (debug)

```bash
curl -v -X POST http://localhost:8081/api/v1/collector/countries
```

### Obtener solo headers de respuesta

```bash
curl -I -X POST http://localhost:8081/api/v1/collector/countries
```

### Ejecutar múltiples veces en secuencia

```bash
for i in {1..3}; do
  echo "Ejecución $i:"
  curl -X POST http://localhost:8081/api/v1/collector/countries | jq '.count'
  sleep 2
done
```

### Medir tiempo de respuesta

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries \
  -w "\nTiempo total: %{time_total}s\n" \
  -o /dev/null -s
```

### Crear reporte HTML de países

```bash
curl -X POST http://localhost:8081/api/v1/collector/countries | \
  jq -r '.data[] | "<li>\(.name) (\(.code))</li>"' > countries.html
```

---

## Health Checks y Monitoring

### Health Check Básico

```bash
curl http://localhost:8081/actuator/health
```

### Health Check Detallado con formato

```bash
curl http://localhost:8081/actuator/health | jq
```

### Ver Métricas Disponibles

```bash
curl http://localhost:8081/actuator/metrics | jq
```

### Ver Métrica Específica (ejemplo: HTTP requests)

```bash
curl "http://localhost:8081/actuator/metrics/http.server.requests" | jq
```

### Ver Uso de Memoria JVM

```bash
curl http://localhost:8081/actuator/metrics/jvm.memory.used | jq
```

---

## Testing

### Test con HTTPie (alternativa a curl)

Si tienes HTTPie instalado:

```bash
# Instalar HTTPie (opcional)
brew install httpie

# Ejecutar request
http POST http://localhost:8081/api/v1/collector/countries
```

### Test con Postman

Importa la siguiente colección en Postman:

```json
{
  "info": {
    "name": "Data Collector Service",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Collect Countries",
      "request": {
        "method": "POST",
        "header": [],
        "url": {
          "raw": "http://localhost:8081/api/v1/collector/countries",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "collector", "countries"]
        }
      }
    },
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8081/actuator/health",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["actuator", "health"]
        }
      }
    }
  ]
}
```

### Script de Test Automatizado

Crea un archivo `test.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8081"

echo "==================================="
echo "Testing Data Collector Service"
echo "==================================="

# Test 1: Health Check
echo -e "\n[TEST 1] Health Check..."
response=$(curl -s "${BASE_URL}/actuator/health")
status=$(echo $response | jq -r '.status')

if [ "$status" == "UP" ]; then
  echo "✅ Health check passed"
else
  echo "❌ Health check failed"
  exit 1
fi

# Test 2: Collect Countries
echo -e "\n[TEST 2] Collect Countries..."
response=$(curl -s -X POST "${BASE_URL}/api/v1/collector/countries")
status=$(echo $response | jq -r '.status')
count=$(echo $response | jq -r '.count')

if [ "$status" == "SUCCESS" ] && [ "$count" -gt 0 ]; then
  echo "✅ Countries collected successfully: $count countries"
else
  echo "❌ Failed to collect countries"
  exit 1
fi

# Test 3: OpenAPI Docs
echo -e "\n[TEST 3] OpenAPI Documentation..."
response=$(curl -s "${BASE_URL}/api-docs")
title=$(echo $response | jq -r '.info.title')

if [ ! -z "$title" ]; then
  echo "✅ OpenAPI docs available: $title"
else
  echo "❌ OpenAPI docs not available"
  exit 1
fi

echo -e "\n==================================="
echo "All tests passed! ✅"
echo "==================================="
```

Ejecutar:

```bash
chmod +x test.sh
./test.sh
```

---

## Scripts Útiles

### Script de Monitoreo Continuo

```bash
#!/bin/bash
# monitor.sh - Monitorea el servicio cada 5 segundos

while true; do
  clear
  echo "=== Data Collector Service Monitor ==="
  echo "Time: $(date)"
  echo ""

  echo "Health Status:"
  curl -s http://localhost:8081/actuator/health | jq '.status'

  echo ""
  echo "Memory Usage:"
  curl -s http://localhost:8081/actuator/metrics/jvm.memory.used | \
    jq '.measurements[0].value' | \
    awk '{printf "%.2f MB\n", $1/1024/1024}'

  sleep 5
done
```

### Script de Extracción de Datos

```bash
#!/bin/bash
# extract_countries.sh - Extrae datos de países en diferentes formatos

BASE_URL="http://localhost:8081"

echo "Recolectando países..."
response=$(curl -s -X POST "${BASE_URL}/api/v1/collector/countries")

# CSV
echo "Creando archivo CSV..."
echo $response | jq -r '.data[] | [.name, .code, .flag] | @csv' > countries.csv

# JSON
echo "Creando archivo JSON..."
echo $response | jq '.data' > countries.json

# Texto plano
echo "Creando archivo de texto..."
echo $response | jq -r '.data[] | "\(.name) - \(.code)"' > countries.txt

echo "✅ Archivos creados: countries.csv, countries.json, countries.txt"
```

---

## Documentación Interactiva

Accede a la documentación interactiva de Swagger UI:

```
http://localhost:8081/swagger-ui.html
```

Desde allí puedes:
- Ver todos los endpoints disponibles
- Probar los endpoints directamente desde el navegador
- Ver esquemas de request/response
- Descargar la especificación OpenAPI

---

## Troubleshooting

### El servicio no responde

```bash
# Verificar si el puerto está en uso
lsof -i :8081

# Ver logs de la aplicación
tail -f logs/application.log
```

### Error de conexión con API-Football

```bash
# Verificar conectividad
curl -I https://api-football-v1.p.rapidapi.com

# Verificar API key en application.yml
grep "rapidapi-key" src/main/resources/application.yml
```

### Error al parsear JSON

```bash
# Validar que la respuesta es JSON válido
curl -X POST http://localhost:8081/api/v1/collector/countries | \
  python -m json.tool
```