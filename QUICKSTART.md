# Quick Start Guide - ElProducto

Guía rápida para empezar a trabajar en el proyecto ElProducto.

## Primer Setup (Solo una vez)

### 1. Clonar el Repositorio
```bash
git clone <repository-url>
cd ElProducto
```

### 2. Instalar Prerrequisitos

#### macOS
```bash
# Instalar Docker Desktop
# Descargar desde: https://www.docker.com/products/docker-desktop

# Instalar GraalVM 21 con Native Image (usando SDKMAN - recomendado)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Instalar GraalVM 21 (NO Java regular)
sdk install java 21-graalce

# Instalar Native Image component
gu install native-image

# Instalar Maven
sdk install maven
```

#### Linux (Ubuntu/Debian)
```bash
# Docker
sudo apt-get update
sudo apt-get install docker.io docker-compose

# Instalar GraalVM 21 con Native Image
# Opción 1: Usando SDKMAN (recomendado)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21-graalce
gu install native-image

# Opción 2: Descarga manual
# Descargar desde: https://www.graalvm.org/downloads/
# Extraer y configurar JAVA_HOME

# Maven
sdk install maven
# O: sudo apt-get install maven
```

### 3. Configurar Variables de Entorno
```bash
# Copiar template
cp .env.example .env

# Editar y agregar tu API key
nano .env  # o tu editor preferido
```

**⚠️ IMPORTANTE:** Obtén tu API key desde https://www.api-football.com/

### 4. Verificar Instalación
```bash
docker --version          # Debe mostrar versión 20.10+
docker-compose --version  # Debe mostrar versión 2.0+
java -version            # Debe mostrar "GraalVM CE 21" o "OpenJDK Runtime Environment GraalVM CE"
native-image --version   # Debe mostrar versión de Native Image
mvn -version             # Debe mostrar Maven 3.9+
```

**⚠️ IMPORTANTE:** `java -version` debe mostrar **GraalVM**, no solo OpenJDK regular. Ejemplo de salida correcta:
```
openjdk version "21.0.1" 2023-10-17
OpenJDK Runtime Environment GraalVM CE 21.0.1+12.1 (build 21.0.1+12-jvmci-23.1-b19)
OpenJDK 64-Bit Server VM GraalVM CE 21.0.1+12.1 (build 21.0.1+12-jvmci-23.1-b19, mixed mode, sharing)
```

## Desarrollo Diario

### Opción 1: Solo Base de Datos (Recomendado para desarrollo)

```bash
# Iniciar PostgreSQL y Redis
./scripts/dev-start.sh

# En otra terminal, ejecutar el microservicio que estés desarrollando
cd backend/data-collector-service
./mvnw spring-boot:run

# O para api-service
cd backend/api-service
./mvnw spring-boot:run
```

### Opción 2: Todo con Docker

```bash
# Construir e iniciar todos los servicios
docker-compose up --build

# En modo detached (background)
docker-compose up -d
```

### Detener Servicios

```bash
# Detener servicios
./scripts/dev-stop.sh

# O si usaste docker-compose directamente
docker-compose down
```

## Estructura del Proyecto

```
ElProducto/
├── backend/              # Microservicios Java/Spring Boot
│   ├── data-collector-service/
│   └── api-service/
├── frontend/             # Aplicación web
│   └── web-app/
├── infrastructure/       # Nginx, monitoring
├── scripts/              # Scripts útiles
└── docs/                 # Documentación
```

## Comandos Útiles

### Docker
```bash
# Ver servicios corriendo
docker-compose ps

# Ver logs
docker-compose logs -f postgres
docker-compose logs -f api-service

# Conectarse a PostgreSQL
docker exec -it elproducto-postgres psql -U postgres -d elproducto

# Conectarse a Redis
docker exec -it elproducto-redis redis-cli

# Limpiar todo (¡CUIDADO! Borra datos)
docker-compose down -v
```

### Backend (Java/Maven)
```bash
cd backend/data-collector-service

# Ejecutar aplicación en modo JVM (desarrollo)
./mvnw spring-boot:run

# Ejecutar tests
./mvnw test

# Build JVM
./mvnw clean package

# Build saltando tests
./mvnw clean package -DskipTests

# Compilar a GraalVM Native Image (producción)
./mvnw -Pnative native:compile

# Ejecutar binario nativo
./target/data-collector-service
```

**💡 Tip:** Durante desarrollo usa modo JVM (`./mvnw spring-boot:run`) para compilación rápida. Para testing de producción usa Native Image.

### Base de Datos
```bash
# Backup
docker exec elproducto-postgres pg_dump -U postgres elproducto > backup.sql

# Restore
docker exec -i elproducto-postgres psql -U postgres -d elproducto < backup.sql

# Ver tablas
docker exec -it elproducto-postgres psql -U postgres -d elproducto -c "\dt"
```

## URLs de Servicios

Cuando todos los servicios estén corriendo:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| PostgreSQL | `localhost:5432` | Base de datos |
| Redis | `localhost:6379` | Cache |
| Data Collector | `http://localhost:8081` | Microservicio de recolección |
| API Service | `http://localhost:8080` | API REST |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | Documentación de API |
| Health Check | `http://localhost:8080/actuator/health` | Estado del servicio |

## Siguiente Paso: Crear Microservicios

Una vez que tengas el setup funcionando, el siguiente paso es crear los proyectos de los microservicios:

```bash
# Para crear el proyecto data-collector-service
cd backend/data-collector-service

# Usar Spring Initializr o crear manualmente
# Ver: docs/epics/epic-01-technical-analysis.md
```

## Recursos

- [Documentación Completa](docs/README.md)
- [Arquitectura del Sistema](docs/architecture.md)
- [Setup con Docker](docs/DOCKER_SETUP.md)
- [Estructura del Proyecto](docs/PROJECT_STRUCTURE.md)
- [Análisis Técnico Epic 1](docs/epics/epic-01-technical-analysis.md)

## Troubleshooting

### Puerto ya en uso
```bash
# Ver qué proceso usa el puerto
lsof -i :5432  # PostgreSQL
lsof -i :6379  # Redis
lsof -i :8080  # API Service

# Matar proceso
kill -9 <PID>
```

### Docker no funciona
```bash
# Reiniciar Docker
# En Mac: Reiniciar Docker Desktop
# En Linux:
sudo systemctl restart docker
```

### Problemas con PostgreSQL
```bash
# Recrear container
docker-compose down
docker volume rm elproducto_postgres_data
docker-compose up -d postgres
```

## Ayuda

Si tienes problemas:
1. Revisa la documentación en `/docs`
2. Verifica los logs: `docker-compose logs -f`
3. Abre un issue en el repositorio

¡Listo para comenzar! 🚀