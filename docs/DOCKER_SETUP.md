# Guía de Setup con Docker

Esta guía te ayudará a configurar el entorno de desarrollo local usando Docker y Docker Compose.

## Prerrequisitos

- [Docker](https://www.docker.com/get-started) instalado (versión 20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) instalado (versión 2.0+)
- Git instalado

## Instalación de Docker

### macOS
```bash
# Usar Docker Desktop para Mac
# Descargar desde: https://www.docker.com/products/docker-desktop
```

### Linux (Ubuntu/Debian)
```bash
# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Agregar usuario al grupo docker
sudo usermod -aG docker $USER

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### Windows
```
Descargar Docker Desktop desde: https://www.docker.com/products/docker-desktop
```

## Configuración Inicial

### 1. Clonar el Repositorio
```bash
git clone <repository-url>
cd ElProducto
```

### 2. Configurar Variables de Entorno
```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar .env y configurar tus valores
nano .env  # o usar tu editor preferido
```

**Variables importantes a configurar:**
```bash
API_FOOTBALL_KEY=tu_api_key_aqui  # Obtener desde https://www.api-football.com/
```

### 3. Verificar Instalación de Docker
```bash
docker --version
docker-compose --version
```

## Comandos Básicos

### Iniciar Servicios

#### Solo Base de Datos y Cache (Para desarrollo del microservicio localmente)
```bash
docker-compose up -d postgres redis
```

#### Todos los Servicios
```bash
docker-compose up -d
```

#### Ver Logs
```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f postgres
docker-compose logs -f redis
docker-compose logs -f api-service
```

### Detener Servicios

```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volumes (¡CUIDADO! Esto borra los datos)
docker-compose down -v
```

### Reiniciar Servicios

```bash
# Reiniciar un servicio específico
docker-compose restart postgres

# Reiniciar todos los servicios
docker-compose restart
```

### Rebuild de Imágenes

```bash
# Rebuild de todas las imágenes
docker-compose build

# Rebuild sin cache
docker-compose build --no-cache

# Rebuild y start
docker-compose up --build
```

## Acceso a los Servicios

Una vez que los servicios estén corriendo:

| Servicio | URL Local | Credenciales |
|----------|-----------|--------------|
| PostgreSQL | `localhost:5432` | user: `postgres`, pass: `postgres`, db: `elproducto` |
| Redis | `localhost:6379` | Sin password en desarrollo |
| API Service | `http://localhost:8080` | - |
| Data Collector | `http://localhost:8081` | - |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | - |

## Gestión de Base de Datos

### Conectarse a PostgreSQL

```bash
# Usando Docker
docker exec -it elproducto-postgres psql -U postgres -d elproducto

# Desde tu máquina (si tienes psql instalado)
psql -h localhost -U postgres -d elproducto
```

### Comandos útiles de PostgreSQL

```sql
-- Ver todas las tablas
\dt

-- Describir una tabla
\d matches

-- Ver datos de una tabla
SELECT * FROM matches LIMIT 10;

-- Salir
\q
```

### Backup de Base de Datos

```bash
# Crear backup
docker exec elproducto-postgres pg_dump -U postgres elproducto > backup.sql

# Restaurar backup
docker exec -i elproducto-postgres psql -U postgres -d elproducto < backup.sql
```

## Gestión de Redis

### Conectarse a Redis

```bash
# Usar redis-cli dentro del container
docker exec -it elproducto-redis redis-cli

# Ver todas las keys
KEYS *

# Ver valor de una key
GET nombre_de_key

# Limpiar todo el cache
FLUSHALL

# Salir
exit
```

## Troubleshooting

### Puerto ya en uso

Si recibes un error de que el puerto ya está en uso:

```bash
# Ver qué proceso está usando el puerto
lsof -i :5432  # Para PostgreSQL
lsof -i :6379  # Para Redis
lsof -i :8080  # Para API Service

# Matar el proceso (reemplazar PID)
kill -9 <PID>

# O cambiar el puerto en docker-compose.yml
ports:
  - "5433:5432"  # Usar puerto 5433 en tu máquina
```

### Contenedores no inician

```bash
# Ver status de contenedores
docker-compose ps

# Ver logs de errores
docker-compose logs

# Revisar health checks
docker inspect elproducto-postgres | grep Health
```

### Problemas de Conexión

```bash
# Verificar que los contenedores estén en la misma red
docker network ls
docker network inspect elproducto_elproducto-network

# Ping entre contenedores
docker exec elproducto-api ping postgres
```

### Limpiar Todo (Reset completo)

```bash
# CUIDADO: Esto elimina TODO
docker-compose down -v
docker system prune -a --volumes
```

## Desarrollo con Hot Reload

Para desarrollo con hot reload (cambios sin rebuild):

1. Ejecuta solo PostgreSQL y Redis:
```bash
docker-compose up -d postgres redis
```

2. Ejecuta tu microservicio localmente desde tu IDE:
```bash
cd data-collector-service
./mvnw spring-boot:run
```

3. Los microservicios se conectarán a PostgreSQL y Redis en Docker.

## Producción

Para deploy en producción, usar `docker-compose.prod.yml`:

```bash
# Build de imágenes
docker-compose -f docker-compose.prod.yml build

# Push a registry
docker push youruser/elproducto-api:latest
docker push youruser/elproducto-collector:latest

# Deploy
docker-compose -f docker-compose.prod.yml up -d
```

## Monitoreo

### Ver estadísticas de recursos

```bash
# Ver uso de recursos
docker stats

# Ver espacio usado
docker system df
```

### Health Checks

```bash
# Verificar health de PostgreSQL
docker exec elproducto-postgres pg_isready -U postgres

# Verificar health de Redis
docker exec elproducto-redis redis-cli ping

# Ver health status en Docker
docker inspect --format='{{json .State.Health}}' elproducto-postgres
```

## Best Practices

1. **Siempre usar `.env` para configuración**: No hardcodear valores sensibles
2. **No commitear `.env`**: Solo commitear `.env.example`
3. **Usar named volumes**: Para persistencia de datos
4. **Health checks**: Siempre definir health checks
5. **Resource limits**: En producción, siempre definir límites
6. **Multi-stage builds**: Para imágenes más pequeñas
7. **No-root user**: Ejecutar containers con usuario no-root
8. **Logs**: Enviar logs a stdout/stderr
9. **Secrets**: Usar Docker secrets o variables de entorno
10. **Networks**: Aislar servicios en redes privadas

## Recursos Adicionales

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot with Docker](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Docker Image](https://hub.docker.com/_/postgres)
- [Redis Docker Image](https://hub.docker.com/_/redis)

## Soporte

Si encuentras problemas:
1. Revisa los logs: `docker-compose logs -f`
2. Verifica el estado: `docker-compose ps`
3. Revisa la documentación oficial de Docker
4. Abre un issue en el repositorio