# ElProducto

Aplicación web de resultados futbolísticos de Argentina, con soporte nativo para dispositivos móviles.

## Descripción

ElProducto es una plataforma para consultar resultados, estadísticas y seguimiento en tiempo real de partidos de fútbol, enfocada principalmente en:

- Ligas argentinas
- Ligas internacionales con participación de equipos argentinos
- Competiciones internacionales (Copa Libertadores, Copa Sudamericana, etc.)
- Mundial de fútbol

## Estructura del Proyecto (Monorepo)

Este repositorio es un **monorepo** que contiene todos los componentes del sistema:

```
ElProducto/
├── backend/                      # Microservicios del backend
│   ├── data-collector-service/  # Recolección de datos de API externa
│   └── api-service/              # API REST para frontend
├── frontend/                     # Aplicaciones frontend
│   └── web-app/                  # App web responsive
├── infrastructure/               # Configuración de infraestructura
│   ├── nginx/                    # Reverse proxy
│   └── monitoring/               # Prometheus, Grafana, etc
├── scripts/                      # Scripts útiles
├── docs/                         # Documentación
├── docker-compose.yml            # Docker Compose para desarrollo
├── docker-compose.prod.yml       # Docker Compose para producción
└── README.md                     # Este archivo
```

## Componentes Principales

### 1. Backend Services (`/backend`)

#### data-collector-service (Puerto 8081)
Microservicio encargado de recolectar datos de API-Football y almacenarlos en PostgreSQL.

**Tecnologías:** GraalVM 21 (Native Image), Spring Boot 3.2, Quartz, WebClient

#### api-service (Puerto 8080)
API REST que expone endpoints para ser consumidos por el frontend.

**Tecnologías:** GraalVM 21 (Native Image), Spring Boot 3.2, Spring Data JPA, Redis

### 2. Frontend (`/frontend`)

#### web-app (Puerto 3000)
Aplicación web responsive exportable a plataformas móviles.

**Tecnologías:** Por definir (React Native / Flutter / PWA)

### 3. Infrastructure (`/infrastructure`)

Configuración de Nginx, monitoring, y deployment.

Ver cada directorio para más detalles (`backend/README.md`, `frontend/README.md`, etc.)

## Documentación

Toda la documentación del proyecto se encuentra en el directorio `/docs`:

- [Arquitectura del Sistema](docs/architecture.md)
- [Requerimientos](docs/requirements.md)
- [Epics y Features](docs/epics/README.md)
- [Setup con Docker](docs/DOCKER_SETUP.md)
- [Análisis Técnico Epic 1](docs/epics/epic-01-technical-analysis.md)

## Quick Start (Desarrollo Local)

**📖 Ver la [Guía de Inicio Rápido Completa](QUICKSTART.md) para instrucciones detalladas**

### Inicio Rápido

```bash
# 1. Copiar archivo de configuración
cp .env.example .env

# 2. Editar .env y agregar tu API key de API-Football

# 3. Iniciar servicios de desarrollo
./scripts/dev-start.sh

# 4. Desarrollar microservicios localmente
cd backend/data-collector-service
./mvnw spring-boot:run
```

**Prerrequisitos:**
- Docker y Docker Compose
- GraalVM 21 (con Native Image)
- Maven 3.9+

Ver más detalles en [QUICKSTART.md](QUICKSTART.md) y [docs/DOCKER_SETUP.md](docs/DOCKER_SETUP.md)

## Estado del Proyecto

MVP en fase de planificación y definición de requerimientos. Decisiones técnicas completadas.

## Stack Tecnológico

### Backend
- **Lenguaje:** Java 21 LTS
- **Runtime:** GraalVM 21 (Native Image)
- **Framework:** Spring Boot 3.2.x
- **Build:** Maven
- **API Externa:** API-Football (RapidAPI)
- **Container:** Docker

### Base de Datos
- **Principal:** PostgreSQL 16
- **Cache:** Redis 7
- **Migrations:** Flyway

### Frontend (Por Definir)
- React Native / Flutter / PWA

### Infraestructura
- **Desarrollo:** Docker Compose
- **Producción:** DigitalOcean / Railway / Fly.io (por definir)
- **Proxy:** Nginx
