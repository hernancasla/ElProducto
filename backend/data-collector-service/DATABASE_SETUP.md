# Configuración de PostgreSQL con Docker

Este documento explica cómo iniciar PostgreSQL usando Docker Compose y conectar la aplicación.

## Requisitos Previos

- Docker instalado
- Docker Compose instalado

## Iniciar PostgreSQL

### 1. Iniciar el contenedor de PostgreSQL

```bash
docker-compose up -d postgres
```

Este comando iniciará PostgreSQL en segundo plano con la siguiente configuración:

- **Base de datos**: `elproducto_db`
- **Usuario**: `elproducto_user`
- **Contraseña**: `elproducto_pass`
- **Puerto**: `5432`

### 2. Verificar que PostgreSQL está corriendo

```bash
docker-compose ps
```

Deberías ver el contenedor `elproducto-postgres` en estado `Up`.

### 3. Ver logs de PostgreSQL

```bash
docker-compose logs -f postgres
```

## Conectarse a PostgreSQL

### Usando psql desde el contenedor

```bash
docker-compose exec postgres psql -U elproducto_user -d elproducto_db
```

### Comandos útiles de psql

```sql
-- Listar tablas
\dt

-- Ver estructura de la tabla countries
\d countries

-- Ver todos los países
SELECT * FROM countries;

-- Contar países
SELECT COUNT(*) FROM countries;

-- Salir de psql
\q
```

## Iniciar la Aplicación

### 1. Compilar el proyecto

```bash
./mvnw clean install
```

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación se conectará automáticamente a PostgreSQL y ejecutará las migraciones de Flyway.

## Migraciones de Base de Datos

Las migraciones se ejecutan automáticamente al iniciar la aplicación usando **Flyway**.

Los scripts de migración se encuentran en:
```
src/main/resources/db/migration/
```

### Scripts actuales:

- **V1__Create_country_table.sql**: Crea la tabla `countries` con:
  - `id`: Identificador autogenerado
  - `name`: Nombre del país (único)
  - `code`: Código ISO del país (único)
  - `flag`: URL de la bandera
  - `created_at`: Fecha de creación
  - `updated_at`: Fecha de actualización

## Detener PostgreSQL

```bash
docker-compose down
```

Para eliminar también los volúmenes (datos):

```bash
docker-compose down -v
```

## Arquitectura de Persistencia

El proyecto utiliza **R2DBC (Reactive Relational Database Connectivity)** para mantener la consistencia con Spring WebFlux:

```
┌─────────────────────────────────────────────────┐
│          CollectCountriesUseCase                │
│              (Application Layer)                 │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│          CountryRepository Interface             │
│              (Domain Port)                       │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│    PostgresCountryRepositoryAdapter (@Primary)  │
│         (Infrastructure Adapter)                 │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│         CountryR2dbcRepository                   │
│        (Spring Data R2DBC)                       │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
              PostgreSQL Database
```

## Configuración de Conexión

La configuración se encuentra en `application.yml`:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/elproducto_db
    username: elproducto_user
    password: elproducto_pass
```

## Troubleshooting

### Error: "Connection refused"

- Verifica que PostgreSQL esté corriendo: `docker-compose ps`
- Verifica que el puerto 5432 no esté siendo usado por otra aplicación

### Error: "Database does not exist"

- Detén y vuelve a crear los contenedores:
  ```bash
  docker-compose down -v
  docker-compose up -d postgres
  ```

### Ver qué está pasando en la base de datos

```bash
# Logs en tiempo real
docker-compose logs -f postgres

# Conectarse a PostgreSQL
docker-compose exec postgres psql -U elproducto_user -d elproducto_db
```