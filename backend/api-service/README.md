# ElProducto API Service

API REST para consulta de resultados deportivos.

## 🚀 Stack Tecnológico

- **Java 21**
- **Spring Boot 3.2.1**
- **PostgreSQL** (base de datos)
- **Redis** (cache)
- **Flyway** (migraciones de base de datos)
- **MapStruct** (mapeo DTO ↔ Entity)
- **SpringDoc OpenAPI** (Swagger documentation)

## 📁 Estructura del Proyecto

```
src/main/java/com/elproducto/api/
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
├── service/             # Business Logic
├── repository/          # Spring Data JPA
├── mapper/              # MapStruct mappers
├── specification/       # Query filters
├── config/              # Spring configuration
├── exception/           # Exception handling
└── ApiServiceApplication.java
```

## 🔌 Endpoints Principales

### Matches
- `GET /api/v1/matches` - Lista de partidos (con filtros y paginación)
- `GET /api/v1/matches/live` - Partidos en vivo
- `GET /api/v1/matches/{id}` - Detalle de un partido
- `GET /api/v1/matches/{id}/events` - Eventos del partido
- `GET /api/v1/matches/{id}/statistics` - Estadísticas
- `GET /api/v1/matches/{id}/lineups` - Alineaciones

### Teams
- `GET /api/v1/teams` - Lista de equipos
- `GET /api/v1/teams/{id}` - Detalle de un equipo
- `GET /api/v1/teams/{id}/matches` - Partidos del equipo
- `GET /api/v1/teams/{id}/statistics` - Estadísticas del equipo

### Leagues
- `GET /api/v1/leagues` - Lista de ligas
- `GET /api/v1/leagues/{id}` - Detalle de una liga
- `GET /api/v1/leagues/{id}/standings` - Tabla de posiciones

### Admin
- `GET /api/v1/admin/migrations` - Lista de migraciones
- `POST /api/v1/admin/migrations/{id}/execute` - Ejecutar migración
- `GET /api/v1/admin/database/tables` - Tablas de la DB
- `GET /api/v1/admin/logs` - Logs del sistema

## 🏃 Ejecución Local

```bash
# Build
./mvnw clean package

# Run
java -jar target/api-service-1.0.0.jar

# Con variables de entorno
DB_URL=jdbc:postgresql://localhost:5432/elproducto \
DB_USER=postgres \
DB_PASSWORD=postgres \
java -jar target/api-service-1.0.0.jar
```

## 🐳 Docker

### Docker Compose (Recomendado)

Levantar todo el stack (PostgreSQL + Redis + API):

```bash
docker-compose up -d
```

La API estará disponible en http://localhost:8080

Para detener:
```bash
docker-compose down
```

Para ver logs:
```bash
docker-compose logs -f api
```

### Docker Solo (API)

```bash
# Build image
docker build -t elproducto-api .

# Run
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/elproducto \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e REDIS_HOST=host.docker.internal \
  elproducto-api
```

## 📄 Documentación API

Una vez ejecutado, acceder a:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

## ⚙️ Variables de Entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `DB_URL` | URL de PostgreSQL | `jdbc:postgresql://localhost:5432/elproducto` |
| `DB_USER` | Usuario de DB | `postgres` |
| `DB_PASSWORD` | Password de DB | `postgres` |
| `REDIS_HOST` | Host de Redis | `localhost` |
| `REDIS_PORT` | Puerto de Redis | `6379` |
| `REDIS_PASSWORD` | Password de Redis | (vacío) |
| `FRONTEND_URL` | URL del frontend | `http://localhost:3000` |
| `CORS_ALLOWED_ORIGINS` | Origins permitidos | `http://localhost:3000` |

## 🧪 Testing

```bash
./mvnw test
```

## ✅ Estado de Implementación

- [x] Entidades JPA (Match, Team, League)
- [x] Repositories con Spring Data JPA
- [x] DTOs y mappers con MapStruct
- [x] Services con lógica de negocio
- [x] Controllers REST completos
- [x] Manejo global de excepciones
- [x] Configuración CORS
- [x] Cache con Redis
- [x] Documentación Swagger/OpenAPI
- [x] Migraciones Flyway con datos de prueba
- [x] Docker y Docker Compose
- [ ] Seguridad para endpoints /admin
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Endpoints de admin funcionales

## 🎯 Próximos Pasos

1. Implementar seguridad Spring Security para /admin
2. Agregar tests unitarios y de integración
3. Implementar endpoints de admin funcionales (migrations, logs, etc.)
4. Implementar endpoints de eventos y estadísticas de partidos
5. Agregar endpoints de alineaciones (lineups)
6. Implementar tabla de posiciones (standings)
