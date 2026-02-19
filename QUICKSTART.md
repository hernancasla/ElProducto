# Quick Start Guide - ElProducto

Guía rápida para empezar a trabajar en el proyecto ElProducto.

> **Estado actual**: API REST completa con datos de prueba + Frontend PWA + Admin Backoffice.
> Ver [NEXT_STEPS.md](./NEXT_STEPS.md) para el roadmap detallado.

## Primer Setup (Solo una vez)

### 1. Clonar el Repositorio
```bash
git clone <repository-url>
cd ElProducto
git checkout claude/sports-results-pwa-g8ci4
```

### 2. Instalar Prerrequisitos

#### Docker (requerido para el backend)
```bash
# macOS: Descargar Docker Desktop desde https://www.docker.com/products/docker-desktop

# Linux (Ubuntu/Debian)
sudo apt-get update
sudo apt-get install docker.io docker-compose-plugin
```

#### Node.js 20+ (requerido para el frontend)
```bash
# macOS (con Homebrew)
brew install node

# Linux
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
```

#### Java 21 (solo si corres el backend fuera de Docker)
```bash
# Con SDKMAN (recomendado)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21-tem

# Verificar
java -version   # Debe mostrar 21
```

### 3. Verificar Instalación
```bash
docker --version        # 20.10+
node --version          # v20+
npm --version           # 10+
java -version           # 21 (solo si usas backend sin Docker)
```

---

## Desarrollo Diario

### Backend (API + Base de Datos + Redis)

```bash
cd backend/api-service

# Levantar todo el stack (PostgreSQL + Redis + API)
docker-compose up -d

# Verificar que está corriendo
curl http://localhost:8080/actuator/health

# Ver logs en tiempo real
docker-compose logs -f api

# Detener
docker-compose down
```

Las migraciones Flyway se ejecutan automáticamente al iniciar e incluyen datos de prueba.

### Frontend (Next.js PWA)

```bash
cd frontend/web-app

# 1. Instalar dependencias (primera vez)
npm install

# 2. Crear archivo de configuración
cat > .env.local << 'EOF'
NEXT_PUBLIC_API_URL=http://localhost:8080
SESSION_SECRET=dev-secret-min-32-characters-long!!
ADMIN_IP_WHITELIST=127.0.0.1,::1
EOF

# 3. Iniciar servidor de desarrollo
npm run dev
```

Abre [http://localhost:3000](http://localhost:3000)

### Admin Backoffice

El admin está en [http://localhost:3000/admin](http://localhost:3000/admin)

```bash
# Generar hash de password para el admin
cd frontend/web-app
node scripts/generate-admin-password.js

# Agregar el hash resultante al .env.local:
# ADMIN_PASSWORD_HASH=$2a$10$xxxxx...
```

Ver [frontend/web-app/ADMIN_README.md](./frontend/web-app/ADMIN_README.md) para configuración detallada.

---

## Estructura del Proyecto

```
ElProducto/
├── backend/
│   ├── api-service/             # Spring Boot REST API ✅
│   │   ├── src/                 # Código fuente Java
│   │   ├── docker-compose.yml   # Stack completo (DB + Redis + API)
│   │   └── Dockerfile
│   └── data-collector-service/  # Recolector de datos (pendiente)
├── frontend/
│   └── web-app/                 # Next.js 14 PWA ✅
│       ├── app/                 # Páginas (App Router)
│       ├── components/          # Componentes React
│       ├── lib/                 # API clients, hooks, stores
│       └── types/               # TypeScript types
├── infrastructure/              # Nginx, monitoring (pendiente)
├── NEXT_STEPS.md                # Roadmap del proyecto
├── DEPLOYMENT_OPTIONS.md        # Opciones de deployment
└── docker-compose.yml           # Solo infra base (PostgreSQL + Redis)
```

---

## URLs de Servicios

Cuando todos los servicios estén corriendo:

| Servicio | URL | Estado |
|----------|-----|--------|
| Frontend | http://localhost:3000 | ✅ Operativo |
| Admin Backoffice | http://localhost:3000/admin | ✅ Operativo |
| API REST | http://localhost:8080 | ✅ Operativo |
| Swagger UI | http://localhost:8080/swagger-ui.html | ✅ Operativo |
| Health Check | http://localhost:8080/actuator/health | ✅ Operativo |
| PostgreSQL | localhost:5432 | ✅ Via Docker |
| Redis | localhost:6379 | ✅ Via Docker |
| Data Collector | http://localhost:8081 | ⏳ Pendiente |

---

## Endpoints de la API

```bash
# Partidos en vivo (2 partidos de prueba incluidos)
curl http://localhost:8080/api/v1/matches/live

# Todos los partidos (paginado)
curl http://localhost:8080/api/v1/matches

# Filtrar por liga (Premier League = 1)
curl "http://localhost:8080/api/v1/matches?leagueId=1"

# Equipos ingleses
curl "http://localhost:8080/api/v1/teams?country=England"

# Partidos de un equipo
curl http://localhost:8080/api/v1/teams/1/matches

# Ligas disponibles
curl http://localhost:8080/api/v1/leagues
```

Ver documentación completa en Swagger: http://localhost:8080/swagger-ui.html

---

## Comandos Útiles

### Docker (desde `backend/api-service/`)
```bash
docker-compose ps                    # Estado de los servicios
docker-compose logs -f api           # Logs del API
docker-compose logs -f postgres      # Logs de PostgreSQL
docker-compose down                  # Parar todo
docker-compose down -v               # Parar y borrar datos (¡cuidado!)

# Conectarse a PostgreSQL
docker exec -it elproducto-db psql -U postgres -d elproducto

# Ver tablas
docker exec -it elproducto-db psql -U postgres -d elproducto -c "\dt"

# Conectarse a Redis
docker exec -it elproducto-cache redis-cli
```

### Backend (Maven, fuera de Docker)
```bash
cd backend/api-service

./mvnw clean package -DskipTests     # Compilar
java -jar target/api-service-1.0.0.jar  # Ejecutar
./mvnw test                          # Tests
```

### Frontend
```bash
cd frontend/web-app

npm run dev      # Desarrollo
npm run build    # Build producción
npm run start    # Servir producción
npm run lint     # Linting
```

---

## Troubleshooting

### Puerto en uso
```bash
lsof -i :8080   # API Service
lsof -i :3000   # Frontend
lsof -i :5432   # PostgreSQL
lsof -i :6379   # Redis

kill -9 <PID>
```

### Docker no inicia
```bash
# macOS: Reiniciar Docker Desktop

# Linux
sudo systemctl restart docker
```

### Base de datos con datos corruptos
```bash
cd backend/api-service
docker-compose down -v       # Borra el volumen de datos
docker-compose up -d         # Flyway recrea el schema + inserta datos de prueba
```

### Frontend no conecta con el API
```bash
# 1. Verificar que el API responde
curl http://localhost:8080/actuator/health

# 2. Verificar .env.local
cat frontend/web-app/.env.local
# Debe tener: NEXT_PUBLIC_API_URL=http://localhost:8080

# 3. Limpiar caché de Next.js
rm -rf frontend/web-app/.next && npm run dev
```

---

## Recursos

- [NEXT_STEPS.md](./NEXT_STEPS.md) - Roadmap y estado del proyecto
- [DEPLOYMENT_OPTIONS.md](./DEPLOYMENT_OPTIONS.md) - Opciones de deployment
- [backend/api-service/README.md](./backend/api-service/README.md) - Documentación del API
- [frontend/web-app/README.md](./frontend/web-app/README.md) - Documentación del frontend
- [frontend/web-app/ADMIN_README.md](./frontend/web-app/ADMIN_README.md) - Documentación del admin
