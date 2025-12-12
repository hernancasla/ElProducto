# Arquitectura del Sistema

## Visión General

ElProducto está diseñado como un sistema distribuido de microservicios para proporcionar información de resultados futbolísticos en tiempo real.

## Componentes del Sistema

### 1. Microservicio de Recolección de Datos

**Responsabilidades:**
- Consumir APIs externas de resultados futbolísticos
- Transformar y normalizar datos de diferentes fuentes
- Almacenar datos crudos y procesados
- Gestionar actualizaciones en tiempo real durante partidos en vivo

**Tecnologías:**
- Lenguaje: Java 21 LTS
- Framework: Spring Boot 3.2.x
- Scheduler: Quartz
- HTTP Client: OpenFeign / WebClient
- Build: Maven
- Container: Docker

**Fuentes de Datos:**
- API-Football (RapidAPI) - API principal de resultados

### 2. Backend API

**Responsabilidades:**
- Exponer endpoints REST para consumo de datos
- Gestionar autenticación y autorización (futuro)
- Implementar lógica de negocio
- Cachear respuestas frecuentes
- Manejar notificaciones push (futuro)

**Tecnologías:**
- Lenguaje: Java 21 LTS
- Framework: Spring Boot 3.2.x
- Base de Datos: PostgreSQL 16
- Cache: Redis 7
- API Documentation: Swagger/OpenAPI 3
- Build: Maven
- Container: Docker

**Endpoints Principales:**
- `GET /api/v1/matches` - Listado de partidos (con filtros y paginación)
- `GET /api/v1/matches/{id}` - Detalle de partido
- `GET /api/v1/matches/live` - Partidos en vivo
- `GET /api/v1/teams` - Equipos
- `GET /api/v1/teams/{id}` - Detalle de equipo
- `GET /api/v1/leagues` - Ligas y competiciones
- `GET /api/v1/leagues/{id}/standings` - Tabla de posiciones
- `GET /api/v1/search` - Búsqueda global

### 3. Frontend Web/Mobile

**Responsabilidades:**
- Interfaz de usuario responsive
- Experiencia mobile-first
- Actualización en tiempo real
- Navegación intuitiva
- Soporte offline básico

**Tecnologías (Por Definir):**
- Framework: TBD
  - Opción A: React Native (iOS + Android + Web)
  - Opción B: Flutter (iOS + Android + Web)
  - Opción C: PWA con React/Vue/Angular
- Estado: TBD (Redux, MobX, Zustand, Context API)
- Real-time: TBD (WebSockets, Server-Sent Events)

## Almacenamiento de Datos

### Base de Datos: PostgreSQL 16

**Decisión:** Base de datos relacional PostgreSQL

**Razones:**
- Datos futbolísticos son altamente relacionales
- Queries complejas para estadísticas y tablas de posiciones
- Integridad referencial importante
- Excelente integración con Spring Data JPA
- Soporte JSONB para datos flexibles

**Schema Principal:**
- `leagues` - Competiciones
- `teams` - Equipos
- `matches` - Partidos
- `match_statistics` - Estadísticas de partidos
- `match_events` - Eventos (goles, tarjetas, sustituciones)
- `match_lineups` - Alineaciones
- `standings` - Tablas de posiciones
- `api_raw_data` - Datos crudos de API (para reprocesamiento)

### Cache: Redis 7

**Uso:**
- Cache de respuestas de API frecuentes
- Partidos en vivo (alta frecuencia de actualización)
- Sesiones de usuario (futuro)
- Rate limiting

### Estrategia de Infraestructura

**Fase MVP - On-Premise:**
- VPS económico (Hetzner, DigitalOcean Droplet)
- Todos los containers en un servidor
- Costo: $10-20/mes

**Fase Producción - Cloud:**
- DigitalOcean (recomendado)
- Managed PostgreSQL y Redis
- Múltiples instancias de microservicios
- Load balancer
- Costo estimado: $50-80/mes

**Alternativas Cloud:**
- Railway.app (~$20-40/mes)
- Fly.io (~$30-50/mes)

## Arquitectura de Containers (Docker)

### Containers del Sistema

```
┌─────────────────────────────────────────────────┐
│           Docker Host / Kubernetes              │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌─────────────────┐  ┌──────────────────┐    │
│  │ data-collector  │  │   api-service    │    │
│  │   (Java 21)     │  │    (Java 21)     │    │
│  │  Spring Boot    │  │   Spring Boot    │    │
│  │    Port 8081    │  │    Port 8080     │    │
│  └────────┬────────┘  └────────┬─────────┘    │
│           │                     │               │
│           └──────────┬──────────┘               │
│                      │                          │
│           ┌──────────▼──────────┐              │
│           │    PostgreSQL 16    │              │
│           │      Port 5432      │              │
│           └─────────────────────┘              │
│                                                 │
│           ┌─────────────────────┐              │
│           │      Redis 7        │              │
│           │      Port 6379      │              │
│           └─────────────────────┘              │
│                                                 │
│  ┌──────────────────────────────────────────┐ │
│  │         Nginx (Reverse Proxy)            │ │
│  │          Ports 80, 443                   │ │
│  └──────────────────────────────────────────┘ │
│                                                 │
└─────────────────────────────────────────────────┘
```

### Docker Compose para Desarrollo

El proyecto incluirá `docker-compose.yml` para desarrollo local con:
- PostgreSQL con persistencia de datos
- Redis
- data-collector-service
- api-service
- Nginx (opcional)
- Volumes para hot-reload en desarrollo

### Docker Compose para Producción

Configuración optimizada para producción:
- Images multi-stage builds
- Health checks
- Restart policies
- Resource limits
- Networks aisladas
- Secrets management

## Flujo de Datos

```
[API-Football (RapidAPI)]
         ↓
[data-collector-service]
    (Docker Container)
         ↓
   [PostgreSQL]
    (Docker Container)
         ↓
   [api-service]
    (Docker Container)
         ↓
      [Redis]
    (Docker Container)
         ↓
   [Nginx Proxy]
    (Docker Container)
         ↓
  [Frontend Web/Mobile]
```

## Consideraciones de Seguridad

- Autenticación de usuarios (TBD: JWT, OAuth)
- Rate limiting en APIs
- Validación de datos de entrada
- Encriptación de datos sensibles
- HTTPS obligatorio

## Escalabilidad

- Arquitectura preparada para escalar horizontalmente
- Cache en múltiples niveles
- CDN para contenido estático
- Load balancing para APIs

## Monitoring y Logging

**Logs:**
- Logback (integrado con Spring Boot)
- Formato JSON estructurado
- Logs de aplicación en volúmenes Docker
- Logs centralizados (futuro: ELK Stack o similar)

**Métricas:**
- Spring Boot Actuator
- Micrometer + Prometheus
- Dashboards Grafana (futuro)

**Health Checks:**
- `/actuator/health` en todos los microservicios
- Docker healthcheck configurado
- Liveness y Readiness probes (Kubernetes)

**Alertas:**
- Monitoring de uptime
- Alertas de errores críticos
- Alertas de performance degradada

## Pipelines CI/CD

**Build:**
- GitHub Actions / GitLab CI
- Build de images Docker multi-stage
- Tests automatizados
- Security scanning

**Deploy:**
- Push a registry (Docker Hub, GitHub Container Registry)
- Deploy automático a staging
- Deploy manual a producción
- Rollback automático en caso de fallo

## Próximos Pasos

1. ✅ Stack tecnológico definido: Java 21 + Spring Boot + PostgreSQL + Docker
2. ✅ Esquema de base de datos diseñado
3. [ ] Crear Docker Compose para desarrollo local
4. [ ] Implementar microservicio de recolección de datos
5. [ ] Poblar base de datos con datos históricos (3 años)
6. [ ] Implementar API service
7. [ ] Definir contratos de API (OpenAPI spec)
8. [ ] Establecer pipelines CI/CD
9. [ ] Deploy inicial a VPS