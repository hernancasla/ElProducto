# Data Collector Service

Microservicio de recolección de datos de API-Football.

## Descripción

Este microservicio se encarga de:
- Consultar APIs externas (API-Football)
- Procesar y transformar datos
- Almacenar datos en repositorio local (actualmente mock en memoria)

## Documentación de la API

La documentación interactiva de la API está disponible mediante Swagger UI:

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api-docs
- **OpenAPI YAML**: http://localhost:8081/api-docs.yaml

## Arquitectura

Arquitectura Hexagonal simplificada:

```
src/main/java/com/elproducto/datacollector/
├── domain/                    # Lógica de negocio pura
│   ├── model/                 # Entidades de dominio (Country)
│   └── port/                  # Interfaces (puertos)
├── application/               # Casos de uso
│   └── usecase/               # Orquestación de lógica
└── infrastructure/            # Adaptadores
    ├── adapter/
    │   ├── http/              # Cliente HTTP (API-Football)
    │   ├── repository/        # Persistencia (mock)
    │   └── rest/              # Controladores REST
    └── config/                # Configuración
```

## Requisitos

- GraalVM 21 (con Native Image)
- Maven 3.9+

## Ejecución

### Modo JVM (Desarrollo - rápido)

```bash
./mvnw spring-boot:run
```

### Modo Native Image (Producción)

```bash
# Compilar
./mvnw -Pnative native:compile

# Ejecutar binario
./target/data-collector-service
```

## API Endpoints

### 1. Recolectar Países

Ejecuta el proceso de recolección de países desde API-Football y los almacena en el repositorio local.

**Endpoint:** `POST /api/v1/collector/countries`

**Descripción:** Consulta la API externa de API-Football para obtener todos los países disponibles con su información completa.

#### Ejemplo de uso con curl:

```bash
# Recolectar países
curl -X POST http://localhost:8081/api/v1/collector/countries \
  -H "Content-Type: application/json"
```

#### Respuesta exitosa (200 OK):

```json
{
  "status": "SUCCESS",
  "message": "Países recolectados exitosamente",
  "count": 165,
  "data": [
    {
      "name": "Argentina",
      "code": "AR",
      "flag": "https://media.api-sports.io/flags/ar.svg"
    },
    {
      "name": "Brazil",
      "code": "BR",
      "flag": "https://media.api-sports.io/flags/br.svg"
    },
    {
      "name": "Spain",
      "code": "ES",
      "flag": "https://media.api-sports.io/flags/es.svg"
    }
  ]
}
```

#### Respuesta de error (500 Internal Server Error):

```json
{
  "status": "ERROR",
  "message": "Error: Connection timeout",
  "count": 0,
  "data": []
}
```

---

## Ejemplos de curl adicionales

### Verificar el estado del servicio (Health Check)

```bash
# Health check básico
curl http://localhost:8081/actuator/health

# Health check detallado
curl http://localhost:8081/actuator/health | jq
```

**Respuesta esperada:**

```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500068036608,
        "free": 123456789012,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

### Ver métricas de la aplicación

```bash
# Ver métricas disponibles
curl http://localhost:8081/actuator/metrics

# Ver métrica específica (ejemplo: memoria JVM)
curl http://localhost:8081/actuator/metrics/jvm.memory.used | jq
```

### Ver información de la aplicación

```bash
# Información general de la app
curl http://localhost:8081/actuator/info
```

### Descargar la especificación OpenAPI

```bash
# Descargar OpenAPI en formato JSON
curl http://localhost:8081/api-docs -o openapi.json

# Descargar OpenAPI en formato YAML
curl http://localhost:8081/api-docs.yaml -o openapi.yaml
```

### Recolección con verbose output

```bash
# Ver respuesta con headers
curl -v -X POST http://localhost:8081/api/v1/collector/countries

# Ver respuesta formateada con jq
curl -X POST http://localhost:8081/api/v1/collector/countries | jq '.'

# Guardar respuesta en archivo
curl -X POST http://localhost:8081/api/v1/collector/countries -o countries.json
```

## Configuración

Las propiedades se encuentran en `src/main/resources/application.yml`:

- `server.port`: Puerto del servicio (8081)
- `api-football.base-url`: URL base de API-Football
- `api-football.rapidapi-key`: API key de RapidAPI

## Estado Actual

- ✅ Arquitectura hexagonal implementada
- ✅ Cliente HTTP con WebClient (compatible GraalVM)
- ✅ Endpoint /api/v1/collector/countries funcionando
- ✅ Documentación OpenAPI/Swagger completa
- ✅ Logging detallado
- ✅ Repositorio mock (en memoria)
- ✅ Health checks y métricas (Actuator)
- ⏳ Integración con PostgreSQL (pendiente)
- ⏳ Scheduler para ejecución periódica (pendiente)

## Archivos de Documentación

- **README.md** - Este archivo (documentación principal)
- **API_EXAMPLES.md** - Ejemplos avanzados y scripts de testing
- **Swagger UI** - http://localhost:8081/swagger-ui.html (cuando el servicio está corriendo)

## Próximos Pasos

1. Integrar PostgreSQL con Flyway
2. Implementar scheduler con Quartz
3. Agregar más endpoints de API-Football (ligas, equipos, jugadores)
4. Tests unitarios e integración
5. CI/CD con GitHub Actions