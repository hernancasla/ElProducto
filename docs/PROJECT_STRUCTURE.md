# Estructura del Proyecto ElProducto

Este documento describe la organización del monorepo ElProducto y las convenciones que seguimos.

## Visión General

ElProducto está organizado como un **monorepo**, lo que significa que todos los componentes del sistema (backend, frontend, infraestructura) viven en un solo repositorio Git. Esta estrategia ofrece varios beneficios:

- **Versionado unificado**: Todos los componentes se versionen juntos
- **Refactoring atómico**: Cambios que afectan múltiples servicios en un solo commit
- **Configuración compartida**: Docker Compose, scripts, documentación
- **CI/CD simplificado**: Un solo pipeline para todo el proyecto
- **Onboarding más fácil**: Nuevo desarrollador clona un solo repo

## Estructura de Directorios

```
ElProducto/
├── backend/                          # Microservicios del backend
│   ├── data-collector-service/       # Servicio de recolección de datos
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/
│   │   │   │   │   └── com/elproducto/collector/
│   │   │   │   │       ├── CollectorApplication.java
│   │   │   │   │       ├── config/
│   │   │   │   │       ├── client/
│   │   │   │   │       ├── domain/
│   │   │   │   │       │   ├── entity/
│   │   │   │   │       │   └── repository/
│   │   │   │   │       ├── service/
│   │   │   │   │       └── scheduler/
│   │   │   │   └── resources/
│   │   │   │       ├── application.yml
│   │   │   │       └── db/migration/
│   │   │   └── test/
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── README.md
│   │
│   ├── api-service/                  # API REST para frontend
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/
│   │   │   │   │   └── com/elproducto/api/
│   │   │   │   │       ├── ApiApplication.java
│   │   │   │   │       ├── config/
│   │   │   │   │       ├── controller/
│   │   │   │   │       ├── dto/
│   │   │   │   │       ├── domain/
│   │   │   │   │       │   ├── entity/
│   │   │   │   │       │   └── repository/
│   │   │   │   │       ├── service/
│   │   │   │   │       ├── mapper/
│   │   │   │   │       └── exception/
│   │   │   │   └── resources/
│   │   │   │       └── application.yml
│   │   │   └── test/
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── README.md
│   │
│   └── README.md                     # Documentación de backend
│
├── frontend/                         # Aplicaciones frontend
│   ├── web-app/                      # App web principal
│   │   ├── public/
│   │   ├── src/
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   ├── services/
│   │   │   ├── store/
│   │   │   ├── hooks/
│   │   │   ├── utils/
│   │   │   └── App.tsx
│   │   ├── Dockerfile
│   │   ├── package.json
│   │   └── README.md
│   │
│   └── README.md                     # Documentación de frontend
│
├── infrastructure/                   # Configuración de infraestructura
│   ├── nginx/                        # Reverse proxy
│   │   ├── nginx.conf
│   │   ├── nginx.prod.conf
│   │   └── ssl/
│   │
│   ├── monitoring/                   # Monitoring stack (futuro)
│   │   ├── prometheus/
│   │   ├── grafana/
│   │   └── loki/
│   │
│   └── README.md                     # Documentación de infraestructura
│
├── scripts/                          # Scripts útiles
│   ├── dev-start.sh
│   ├── dev-stop.sh
│   ├── dev-reset.sh
│   ├── db-backup.sh
│   ├── db-restore.sh
│   ├── build-all.sh
│   ├── docker-build.sh
│   └── README.md
│
├── docs/                             # Documentación del proyecto
│   ├── architecture.md
│   ├── requirements.md
│   ├── DOCKER_SETUP.md
│   ├── PROJECT_STRUCTURE.md          # Este archivo
│   └── epics/
│       ├── README.md
│       ├── epic-01-technical-analysis.md
│       ├── epic-02-backend-api.md
│       └── ...
│
├── docker-compose.yml                # Compose para desarrollo
├── docker-compose.prod.yml           # Compose para producción
├── .env.example                      # Template de variables de entorno
├── .gitignore
├── LICENSE
└── README.md                         # Documentación principal
```

## Convenciones por Directorio

### `/backend`

**Propósito:** Contiene todos los microservicios del backend.

**Convenciones:**
- Cada microservicio es un proyecto Maven independiente
- Usar package base `com.elproducto.[service]`
- Seguir arquitectura en capas (controller → service → repository)
- Incluir Dockerfile en la raíz de cada servicio
- README.md específico por servicio

**Estructura de paquetes:**
```
com.elproducto.[service]/
├── [Service]Application.java    # Main class
├── config/                       # Configuraciones de Spring
├── controller/                   # REST Controllers (solo en api-service)
├── service/                      # Lógica de negocio
├── domain/                       # Modelo de dominio
│   ├── entity/                   # JPA Entities
│   └── repository/               # Spring Data Repositories
├── dto/                          # Data Transfer Objects
├── mapper/                       # Mappers (Entity <-> DTO)
├── client/                       # Feign clients (solo en data-collector)
├── scheduler/                    # Jobs programados
├── exception/                    # Custom exceptions
└── util/                         # Utilidades
```

### `/frontend`

**Propósito:** Contiene las aplicaciones frontend.

**Convenciones:**
- Por ahora solo `web-app`, pero preparado para mobile apps futuras
- Estructura según framework elegido (React/Flutter/etc)
- Variables de entorno con prefijo `REACT_APP_` o similar
- Documentación de componentes principales

### `/infrastructure`

**Propósito:** Todo lo relacionado con infraestructura, deployment y operaciones.

**Subdirectorios:**
- `nginx/` - Configuración del reverse proxy
- `monitoring/` - Prometheus, Grafana, Loki (futuro)
- `terraform/` - Infrastructure as Code (futuro)
- `kubernetes/` - Manifiestos K8s (futuro)

### `/scripts`

**Propósito:** Scripts útiles para desarrollo, deployment y mantenimiento.

**Convenciones:**
- Usar shell scripts (`.sh`) para Unix/Mac
- Nombres descriptivos en formato `action-target.sh` (ej: `build-all.sh`)
- Todos los scripts deben ser ejecutables: `chmod +x`
- Incluir header con descripción y uso
- Usar `set -e` para exit on error
- Documentar cada script en `scripts/README.md`

### `/docs`

**Propósito:** Toda la documentación del proyecto.

**Organización:**
- Archivos raíz para temas generales (architecture.md, requirements.md)
- Subdirectorio `/epics` para user stories y planning
- Markdown para todo
- Diagramas en formato que funcione en GitHub (Mermaid, ASCII art)

## Workflow de Desarrollo

### 1. Desarrollo de un Microservicio

```bash
# 1. Levantar dependencias (DB, cache)
docker-compose up -d postgres redis

# 2. Desarrollar localmente
cd backend/data-collector-service
./mvnw spring-boot:run

# 3. Tests
./mvnw test

# 4. Build
./mvnw clean package

# 5. Dockerfile build (opcional)
docker build -t elproducto-collector:dev .
```

### 2. Desarrollo de Frontend

```bash
# 1. Asegurar que backend esté corriendo
docker-compose up -d api-service

# 2. Desarrollar frontend
cd frontend/web-app
npm install
npm run dev

# 3. Build
npm run build
```

### 3. Testing Completo

```bash
# Levantar todo el stack
docker-compose up --build

# Verificar que todo funcione
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:3000
```

## Versionado

### Git Flow

Usamos Git Flow modificado:

**Branches principales:**
- `main` - Código en producción
- `develop` - Código de desarrollo

**Branches de features:**
- `feature/nombre-feature` - Nuevas features
- `bugfix/nombre-bug` - Correcciones de bugs
- `hotfix/nombre-hotfix` - Fixes urgentes en producción

**Convención de commits:**
```
tipo(scope): descripción corta

Descripción más larga si es necesario

Tipos: feat, fix, docs, style, refactor, test, chore
Scopes: backend, frontend, infra, docs

Ejemplos:
feat(backend): implementar endpoint de partidos en vivo
fix(frontend): corregir bug en tabla de posiciones
docs(epic1): actualizar análisis técnico
```

### Versionado Semántico

Seguimos SemVer (X.Y.Z):
- `X` (Major): Cambios incompatibles en API
- `Y` (Minor): Nueva funcionalidad compatible
- `Z` (Patch): Correcciones de bugs

## Build y Deployment

### Desarrollo Local
```bash
docker-compose up -d
```

### Staging/QA
```bash
docker-compose -f docker-compose.prod.yml up -d
```

### Producción
```bash
# Build de imágenes con versión
./scripts/docker-build.sh 1.0.0

# Push al registry
./scripts/docker-push.sh 1.0.0

# Deploy
./scripts/deploy-prod.sh 1.0.0
```

## CI/CD

Pipeline automático en GitHub Actions:

1. **Pull Request:**
   - Linter
   - Tests unitarios
   - Build de cada servicio

2. **Merge a `develop`:**
   - Build completo
   - Tests de integración
   - Deploy a ambiente de staging

3. **Merge a `main`:**
   - Build de producción
   - Tag de versión
   - Push a Docker registry
   - Deploy a producción (con aprobación manual)

## Mejores Prácticas

### General
- ✅ Documentar cambios importantes
- ✅ Actualizar README cuando cambie funcionalidad
- ✅ Mantener .gitignore actualizado
- ✅ No commitear secrets ni .env
- ✅ Code review antes de merge

### Backend (Java)
- ✅ Seguir convenciones de Spring Boot
- ✅ Tests con >70% coverage
- ✅ DTOs para comunicación externa
- ✅ Validación de inputs
- ✅ Manejo de excepciones apropiado

### Frontend
- ✅ Componentes reutilizables
- ✅ Mobile-first design
- ✅ Accessibility básico
- ✅ Loading y error states
- ✅ Code splitting

### Docker
- ✅ Multi-stage builds
- ✅ No-root user
- ✅ Health checks
- ✅ .dockerignore apropiado
- ✅ Image tagging semántico

## Recursos Útiles

- [Arquitectura del Sistema](architecture.md)
- [Setup con Docker](DOCKER_SETUP.md)
- [Análisis Técnico Epic 1](epics/epic-01-technical-analysis.md)
- [Requerimientos](requirements.md)

## Preguntas Frecuentes

**Q: ¿Por qué monorepo en lugar de repos separados?**
A: Para este proyecto pequeño/mediano, un monorepo simplifica el desarrollo, deployment y versionado. Si el proyecto crece mucho, se puede migrar a repos separados.

**Q: ¿Cómo agrego un nuevo microservicio?**
A:
1. Crear directorio en `/backend`
2. Configurar como proyecto Maven
3. Agregar al `docker-compose.yml`
4. Documentar en `/backend/README.md`

**Q: ¿Dónde van los archivos de configuración compartidos?**
A: En la raíz del proyecto (`.env`, `docker-compose.yml`) o en `/infrastructure` si es específico de infra.

**Q: ¿Cómo manejo dependencias compartidas entre servicios?**
A: Cada servicio debe ser independiente. Si hay mucho código compartido, considerar crear una librería Maven interna.

---

**Última actualización:** 2024-11-24
**Versión:** 1.0