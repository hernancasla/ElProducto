# Epic 1: Análisis Técnico e Implementación - Recolección de Datos

## Índice
1. [Análisis de APIs de Resultados Futbolísticos](#1-análisis-de-apis)
2. [Decisión de Base de Datos](#2-decisión-de-base-de-datos)
3. [Infraestructura Cloud vs On-Premise](#3-infraestructura)
4. [Stack Tecnológico](#4-stack-tecnológico)
5. [Docker y Containerización](#5-docker-y-containerización)
6. [Modelado de Datos](#6-modelado-de-datos)
7. [Arquitectura de Microservicios](#7-arquitectura-de-microservicios)
8. [Plan de Implementación](#8-plan-de-implementación)

---

## 1. Análisis de APIs de Resultados Futbolísticos

### Opción 1: API-Football (RapidAPI)
**URL:** https://www.api-football.com/

**Características:**
- Cobertura: 200+ ligas mundiales, incluye todas las ligas argentinas
- Datos: Partidos, estadísticas, alineaciones, eventos en vivo, standings
- Actualización: Tiempo real (durante partidos en vivo)
- Datos históricos: Hasta 10+ años atrás
- Formato: REST API, JSON
- Rate Limiting: Según plan

**Pricing:**
- Free: 100 requests/día (muy limitado)
- Basic: $15/mes - 3,000 requests/día
- Pro: $30/mes - 30,000 requests/día
- Ultra: $60/mes - 60,000 requests/día

**Endpoints Principales:**
```
GET /fixtures - Partidos (por fecha, liga, equipo)
GET /fixtures/statistics - Estadísticas de partido
GET /fixtures/events - Eventos del partido
GET /fixtures/lineups - Alineaciones
GET /standings - Tabla de posiciones
GET /teams - Información de equipos
GET /leagues - Ligas disponibles
```

**Pros:**
- ✅ Muy completa, cubre todo lo necesario
- ✅ Documentación excelente
- ✅ Cobertura de ligas argentinas confirmada
- ✅ Datos históricos extensos
- ✅ Comunidad activa

**Contras:**
- ❌ Costo puede escalar rápido con usuarios
- ❌ Dependencia de RapidAPI
- ❌ Rate limits pueden ser restrictivos

**Recomendación para MVP:** ⭐⭐⭐⭐ (4/5)

---

### Opción 2: Football-Data.org
**URL:** https://www.football-data.org/

**Características:**
- Cobertura: Ligas europeas principalmente, limitada en Sudamérica
- Datos: Partidos, standings, equipos
- Actualización: Cada 1-2 minutos
- Datos históricos: Varias temporadas
- Formato: REST API, JSON

**Pricing:**
- Free Tier: 10 requests/minuto (suficiente para MVP)
- Paid plans: €19-€59/mes

**Endpoints Principales:**
```
GET /competitions - Ligas
GET /matches - Partidos
GET /standings - Tablas
GET /teams - Equipos
```

**Pros:**
- ✅ Free tier generoso
- ✅ Muy económica
- ✅ API simple y clara
- ✅ Confiable

**Contras:**
- ❌ Cobertura limitada de ligas argentinas
- ❌ Menos datos estadísticos
- ❌ No alineaciones ni eventos detallados
- ❌ No Copa Libertadores/Sudamericana confirmada

**Recomendación para MVP:** ⭐⭐ (2/5) - No ideal para foco argentino

---

### Opción 3: SportMonks Football API
**URL:** https://www.sportmonks.com/football-api/

**Características:**
- Cobertura: 2,000+ ligas, muy completa en Latinoamérica
- Datos: Partidos, estadísticas, alineaciones, eventos, odds, lesiones
- Actualización: Tiempo real
- Datos históricos: Extensos
- Formato: REST API, JSON

**Pricing:**
- Starter: €39/mes - 5,000 requests/día
- Basic: €79/mes - 15,000 requests/día
- Premium: €179/mes - 50,000 requests/día

**Endpoints Principales:**
```
GET /fixtures - Partidos
GET /leagues - Ligas
GET /teams - Equipos
GET /standings - Tablas
GET /lineups - Alineaciones
GET /events - Eventos del partido
```

**Pros:**
- ✅ Excelente cobertura Sudamérica
- ✅ Datos muy completos
- ✅ Documentación excelente
- ✅ Includes Copa Libertadores, Sudamericana
- ✅ Sin límite de requests en planes pagos (solo rate limit)

**Contras:**
- ❌ Más costosa que otras opciones
- ❌ No hay free tier real (trial limitado)

**Recomendación para MVP:** ⭐⭐⭐⭐⭐ (5/5)

---

### Comparativa y Recomendación Final

| Criterio | API-Football | Football-Data | SportMonks |
|----------|--------------|---------------|------------|
| Cobertura Argentina | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| Datos completos | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Costo/Beneficio | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Documentación | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Free tier | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐ |
| Históricos | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

**Recomendación:**
1. **Para MVP (sin presupuesto):** API-Football plan Free (100 req/día) + cacheo agresivo
2. **Para MVP (presupuesto bajo):** API-Football plan Basic ($15/mes)
3. **Para producción:** SportMonks (mejor cobertura Argentina) o API-Football Pro

**Decisión sugerida:** Comenzar con **API-Football plan Basic** y evaluar migrar a SportMonks si el producto tiene tracción.

---

## 2. Decisión de Base de Datos

### Análisis: Relacional vs NoSQL

#### Opción A: PostgreSQL (Relacional)

**Características:**
- Relacional, ACID compliant
- Excelente para datos estructurados
- Queries complejas con JOINs eficientes
- Integridad referencial
- Indices, vistas, stored procedures

**Pros para este proyecto:**
- ✅ Datos de fútbol son altamente relacionales (equipos ↔ partidos ↔ jugadores)
- ✅ Queries complejas (estadísticas, historial) son comunes
- ✅ Transacciones ACID importantes para consistencia
- ✅ Madurez y estabilidad probada
- ✅ Excelente integración con Spring Boot (JPA/Hibernate)
- ✅ Soporte JSON para datos flexibles (JSONB)
- ✅ Full-text search integrado
- ✅ Escalabilidad vertical excelente

**Contras:**
- ❌ Escalabilidad horizontal más compleja
- ❌ Schema rígido (mitigado con migrations)

**Estimación de storage (3 años):**
- ~50 ligas * 380 partidos/año * 3 años = ~57,000 partidos
- Con estadísticas: ~500KB por partido promedio
- Total: ~28.5 GB (muy manejable)

---

#### Opción B: MongoDB (NoSQL - Document)

**Características:**
- NoSQL orientado a documentos
- Schema flexible
- Escalabilidad horizontal nativa
- Queries en JSON

**Pros:**
- ✅ Flexibilidad de schema
- ✅ Escalabilidad horizontal fácil
- ✅ Buen performance en lecturas
- ✅ Documentos anidados naturales

**Contras:**
- ❌ Joins menos eficientes
- ❌ No ACID multi-documento (hasta versiones recientes)
- ❌ Queries complejas más difíciles
- ❌ Denormalización puede llevar a redundancia
- ❌ Menos ideal para datos altamente relacionales

---

#### Opción C: Enfoque Híbrido

**PostgreSQL** (datos estructurados) + **Redis** (cache)

**Ventajas:**
- ✅ Lo mejor de ambos mundos
- ✅ PostgreSQL para datos relacionales
- ✅ Redis para cache de lecturas frecuentes
- ✅ Redis para partidos en vivo (alta frecuencia)

---

### Recomendación: PostgreSQL + Redis

**Razones:**
1. Datos de fútbol son inherentemente relacionales
2. Necesitamos queries complejas (standings, estadísticas)
3. Volumen de datos no justifica NoSQL (< 100GB en años)
4. Integridad referencial es importante
5. Spring Data JPA excelente con PostgreSQL
6. JSONB permite flexibilidad donde se necesite
7. Redis complementa para performance

**Configuración sugerida:**
- **PostgreSQL 16** (última estable) para datos principales
- **Redis 7** para cache y sesiones
- **pgAdmin 4** para administración
- **Flyway** para migrations

---

## 3. Infraestructura: On-Premise → Cloud

### Fase 1: On-Premise (MVP)

**Setup inicial:**
- Servidor local o VPS (DigitalOcean, Linode, Hetzner)
- PostgreSQL en mismo servidor
- Redis en mismo servidor
- Nginx como reverse proxy

**Costo estimado:** $10-20/mes (VPS básico)

---

### Fase 2: Migración a Cloud (Post-MVP)

#### Opción 1: DigitalOcean (Recomendado)

**Pros:**
- ✅ Mucho más económico que AWS
- ✅ UI simple e intuitiva
- ✅ Managed PostgreSQL disponible
- ✅ Managed Redis disponible
- ✅ Kubernetes (DOKS) si escalamos
- ✅ Muy buena relación precio/performance
- ✅ Data centers en varias regiones

**Pricing:**
- App Platform: desde $5/mes por servicio
- Managed PostgreSQL: desde $15/mes (1GB RAM, 10GB storage)
- Managed Redis: desde $15/mes
- Droplets (VPS): desde $6/mes

**Costo estimado completo:** ~$50-80/mes

---

#### Opción 2: Railway.app

**Pros:**
- ✅ Muy económico para start
- ✅ Deploy extremadamente fácil
- ✅ PostgreSQL incluido
- ✅ Redis incluido
- ✅ $5 free credits/mes

**Pricing:**
- Pay per use: ~$0.000231/GB-hour (memoria)
- Storage: ~$0.25/GB/mes

**Costo estimado:** ~$20-40/mes

---

#### Opción 3: Fly.io

**Pros:**
- ✅ Global edge deployment
- ✅ Pricing competitivo
- ✅ Excelente para baja latencia
- ✅ Free tier generoso

**Pricing:**
- Free: 3 VMs shared-cpu + 3GB storage
- Paid: ~$30-50/mes para producción básica

---

### Recomendación Cloud: DigitalOcean

**Razones:**
1. Balance perfecto costo/performance/features
2. Managed databases simplifican ops
3. Fácil escalar cuando sea necesario
4. Buena documentación
5. Soporte decente

**Plan de migración:**
1. MVP en VPS económico (Hetzner/DigitalOcean Droplet)
2. Al llegar a 1000 usuarios: Managed databases
3. Al llegar a 10k usuarios: Múltiples instancias + load balancer
4. Al llegar a 50k: Kubernetes (DOKS)

---

## 4. Stack Tecnológico

### Backend - Microservicio de Recolección

```
Java: 21 LTS (última versión estable long-term)
Framework: Spring Boot 3.2.x
Build: Maven 3.9.x o Gradle 8.x
```

**Dependencias Core:**

```xml
<!-- Spring Boot Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Redis para cache -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- HTTP Client - OpenFeign (compatible con Native) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- O WebClient (reactive, Native-friendly) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- Scheduling -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Flyway para migrations -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Lombok (opcional, reduce boilerplate) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Actuator para health checks -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Micrometer para métricas -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

---

### ⚡ GraalVM Native Image (OBLIGATORIO)

**⚠️ IMPORTANTE:** Este proyecto usa **GraalVM Native Image** desde el inicio, no como opción futura.

**Razones para usar GraalVM Native:**
- ⚡ **Startup instantáneo**: ~100ms vs ~10 segundos JVM tradicional
- 💾 **Memoria reducida**: ~50-100MB vs 300-500MB JVM
- 📦 **Binarios independientes**: No requiere JVM en producción
- 💰 **Costos reducidos**: Menos memoria = menos infraestructura
- 🚀 **Performance**: Optimizaciones ahead-of-time

**Configuración Maven para Native Image:**

```xml
<!-- Plugin de GraalVM Native Build Tools -->
<plugin>
    <groupId>org.graalvm.buildtools</groupId>
    <artifactId>native-maven-plugin</artifactId>
    <version>0.9.28</version>
    <extensions>true</extensions>
    <executions>
        <execution>
            <id>build-native</id>
            <goals>
                <goal>compile-no-fork</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <imageName>${project.artifactId}</imageName>
        <mainClass>${start-class}</mainClass>
        <buildArgs>
            <buildArg>--no-fallback</buildArg>
            <buildArg>-H:+ReportExceptionStackTraces</buildArg>
        </buildArgs>
    </configuration>
</plugin>
```

**Profile de Maven para Native:**

```xml
<profiles>
    <profile>
        <id>native</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.graalvm.buildtools</groupId>
                    <artifactId>native-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

**Comandos de Build:**

```bash
# Desarrollo (JVM normal - más rápido para iterar)
./mvnw spring-boot:run

# Testing (Native Image - producción)
./mvnw -Pnative native:compile
./target/data-collector-service

# Build Docker con Native
docker build -f Dockerfile.native -t service:native .
```

---

**⚠️ IMPORTANTE - Compatibilidad con GraalVM Native:**

### ✅ Librerías COMPATIBLES (Verificadas con Spring Boot 3.2+)
- Spring Boot 3.2.x (soporte nativo completo)
- Spring Data JPA (con configuraciones específicas)
- PostgreSQL Driver (org.postgresql:postgresql)
- Redis (Lettuce - driver por defecto)
- Flyway (migrations)
- WebClient (Spring WebFlux)
- Quartz Scheduler (con hints de reflection)
- Jackson (JSON serialization)
- Micrometer (metrics)
- Logback (logging)
- Spring Actuator
- Swagger/Springdoc OpenAPI

### ❌ Librerías INCOMPATIBLES - EVITAR
- **RestTemplate** → Usar WebClient
- **Apache HttpClient** → Usar WebClient
- **OpenFeign sin configuración** → Preferir WebClient
- **Reflection dinámica sin hints**
- **CGLIB proxies excesivos**
- **Lazy loading de Hibernate** → Usar EAGER o fetch joins

### 📝 Best Practices OBLIGATORIAS para Native Image

**1. SIEMPRE usar WebClient (no RestTemplate)**
```java
// ❌ NO - RestTemplate no es óptimo para Native
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}

// ✅ SI - WebClient es totalmente compatible
@Bean
public WebClient webClient() {
    return WebClient.builder()
        .baseUrl("${api.football.url}")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "${api.football.key}")
        .build();
}
```

**2. SIEMPRE usar Records para DTOs (Java 21)**
```java
// ✅ Records son óptimos para Native (menos reflection)
public record MatchDto(
    Long id,
    String homeTeam,
    String awayTeam,
    Integer homeGoals,
    Integer awayGoals,
    LocalDateTime matchDate
) {}

// En lugar de clases tradicionales con getters/setters
```

**3. Configurar JPA para Native Image**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none  # SIEMPRE usar Flyway, nunca auto DDL
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
    open-in-view: false  # Evitar lazy loading issues
```

**4. Hints de Reflection para Entities**
```java
// En Application.java o clase de configuración
@RegisterReflectionForBinding({
    Match.class,
    Team.class,
    League.class,
    MatchStatistics.class
    // ... todas las entities
})
@SpringBootApplication
public class DataCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataCollectorApplication.class, args);
    }
}
```

**5. Configuración de Quartz para Native**
```yaml
spring:
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: never  # Usar Flyway
    properties:
      org:
        quartz:
          scheduler:
            instanceId: AUTO
          threadPool:
            threadCount: 5
```

**6. Testing con Native Image**
```bash
# Compilar nativo (tarda 2-5 minutos primera vez)
./mvnw -Pnative native:compile

# Tests con Native Image
./mvnw -PnativeTest test
```

---

### Dockerfile Optimizado para GraalVM Native

**Opción 1: Dockerfile.native (Multi-stage con GraalVM)**

```dockerfile
# Stage 1: Build con GraalVM
FROM ghcr.io/graalvm/native-image:ol9-java21-22.3.3 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

# Build nativo
RUN ./mvnw -Pnative native:compile

# Stage 2: Runtime (distroless o alpine)
FROM gcr.io/distroless/base-debian11

WORKDIR /app
COPY --from=builder /build/target/data-collector-service .

# Usuario no-root
USER nonroot:nonroot

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD ["/app/data-collector-service", "--health"]

EXPOSE 8081

ENTRYPOINT ["/app/data-collector-service"]
```

**Opción 2: Dockerfile normal (Spring Boot con GraalVM)**

```dockerfile
# Stage 1: Build
FROM maven:3.9-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime con GraalVM JIT
FROM ghcr.io/graalvm/jdk:java21-22.3.3

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN addgroup --system appuser && adduser --system --ingroup appuser appuser
USER appuser

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Comparación de Imágenes:**

| Tipo | Tamaño | Startup | Memoria | Uso |
|------|--------|---------|---------|-----|
| JVM Normal | ~300MB | ~10s | ~500MB | Desarrollo |
| GraalVM JIT | ~250MB | ~8s | ~400MB | Staging |
| GraalVM Native | ~80MB | ~0.1s | ~100MB | Producción |

---

### Troubleshooting GraalVM Native

**Problema: ClassNotFoundException en runtime**
```bash
# Solución: Agregar hint de reflection
@RegisterReflectionForBinding(MiClase.class)
```

**Problema: Lazy loading exception**
```java
// Solución: Usar EAGER fetch o fetch join
@ManyToOne(fetch = FetchType.EAGER)
private Team team;

// O en query
@Query("SELECT m FROM Match m JOIN FETCH m.team")
```

**Problema: Build nativo falla**
```bash
# Ver logs detallados
./mvnw -Pnative -X native:compile

# Verificar compatibilidad
./mvnw -Pnative native:metadata-copy
```

---

### Estructura del Proyecto

```
data-collector-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/elproducto/collector/
│   │   │       ├── CollectorApplication.java
│   │   │       ├── config/
│   │   │       │   ├── ApiClientConfig.java
│   │   │       │   ├── DatabaseConfig.java
│   │   │       │   ├── RedisConfig.java
│   │   │       │   └── SchedulerConfig.java
│   │   │       ├── client/
│   │   │       │   ├── ApiFootballClient.java (Feign)
│   │   │       │   └── dto/          (DTOs de API externa)
│   │   │       ├── domain/
│   │   │       │   ├── entity/       (JPA Entities)
│   │   │       │   └── repository/   (Spring Data repos)
│   │   │       ├── service/
│   │   │       │   ├── CollectionService.java
│   │   │       │   ├── TransformService.java
│   │   │       │   └── StorageService.java
│   │   │       ├── scheduler/
│   │   │       │   └── MatchCollectionJob.java
│   │   │       └── util/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/        (Flyway migrations)
│   │           └── V1__initial_schema.sql
│   └── test/
├── pom.xml
└── README.md
```

---

## 5. Docker y Containerización

### Estrategia de Containerización

Todos los microservicios serán containerizados con Docker para:
- Facilitar desarrollo local consistente
- Simplificar deploys
- Permitir escalabilidad horizontal
- Garantizar consistencia entre entornos

### Dockerfiles

#### Dockerfile para Microservicios Java/Spring Boot

**Opción 1: Multi-stage build (Recomendado)**

```dockerfile
# Dockerfile - data-collector-service
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build del proyecto
RUN mvn clean package -DskipTests

# Imagen final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser

COPY --from=build /app/target/*.jar app.jar

# Cambiar a usuario no-root
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Opción 2: Con Spring Boot Layered JAR (Optimizado)**

```dockerfile
# Dockerfile - Optimizado con layers
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Imagen final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser

ARG DEPENDENCY=/app/target/dependency

# Copiar layers separados para mejor caching
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

USER appuser

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

ENTRYPOINT ["java","-cp","app:app/lib/*","com.elproducto.collector.CollectorApplication"]
```

#### Dockerfile para api-service

```dockerfile
# Dockerfile - api-service
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser

COPY --from=build /app/target/*.jar app.jar

USER appuser

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose - Desarrollo

**`docker-compose.yml`** para desarrollo local:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: elproducto-postgres
    environment:
      POSTGRES_DB: elproducto
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init-scripts:/docker-entrypoint-initdb.d
    networks:
      - elproducto-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    container_name: elproducto-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - elproducto-network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 3s
      retries: 5

  data-collector:
    build:
      context: ./data-collector-service
      dockerfile: Dockerfile
    container_name: elproducto-collector
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/elproducto
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
      API_FOOTBALL_KEY: ${API_FOOTBALL_KEY}
      API_FOOTBALL_URL: https://v3.football.api-sports.io
    ports:
      - "8081:8081"
    networks:
      - elproducto-network
    volumes:
      # Hot reload para desarrollo (opcional)
      - ./data-collector-service/target:/app/target
    restart: unless-stopped

  api-service:
    build:
      context: ./api-service
      dockerfile: Dockerfile
    container_name: elproducto-api
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/elproducto
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
    ports:
      - "8080:8080"
    networks:
      - elproducto-network
    restart: unless-stopped

  # Opcional: Nginx como reverse proxy
  nginx:
    image: nginx:alpine
    container_name: elproducto-nginx
    depends_on:
      - api-service
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
    networks:
      - elproducto-network
    restart: unless-stopped

networks:
  elproducto-network:
    driver: bridge

volumes:
  postgres_data:
    driver: local
  redis_data:
    driver: local
```

### Docker Compose - Producción

**`docker-compose.prod.yml`**:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: elproducto-postgres-prod
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - elproducto-network
    restart: always
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER}"]
      interval: 10s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    container_name: elproducto-redis-prod
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    networks:
      - elproducto-network
    restart: always
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M

  data-collector:
    image: ${DOCKER_REGISTRY}/elproducto-collector:${VERSION}
    container_name: elproducto-collector-prod
    depends_on:
      - postgres
      - redis
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PASSWORD: ${REDIS_PASSWORD}
      API_FOOTBALL_KEY: ${API_FOOTBALL_KEY}
      JAVA_OPTS: "-Xms512m -Xmx1g"
    networks:
      - elproducto-network
    restart: always
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 1.5G
      replicas: 1

  api-service:
    image: ${DOCKER_REGISTRY}/elproducto-api:${VERSION}
    container_name: elproducto-api-prod
    depends_on:
      - postgres
      - redis
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PASSWORD: ${REDIS_PASSWORD}
      JAVA_OPTS: "-Xms512m -Xmx1g"
    networks:
      - elproducto-network
    restart: always
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 1.5G
      replicas: 2  # Multiple instancias para alta disponibilidad

  nginx:
    image: nginx:alpine
    container_name: elproducto-nginx-prod
    depends_on:
      - api-service
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.prod.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
      - /var/log/nginx:/var/log/nginx
    networks:
      - elproducto-network
    restart: always

networks:
  elproducto-network:
    driver: bridge

volumes:
  postgres_data:
    driver: local
  redis_data:
    driver: local
```

### Archivo .env

**`.env`** (para desarrollo):

```bash
# API Externa
API_FOOTBALL_KEY=your_api_key_here

# PostgreSQL
POSTGRES_DB=elproducto
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Redis
REDIS_PASSWORD=

# Docker Registry (para producción)
DOCKER_REGISTRY=docker.io/youruser
VERSION=latest
```

### Comandos Docker útiles

```bash
# Desarrollo
docker-compose up -d                    # Levantar todos los servicios
docker-compose up -d postgres redis     # Solo BD y cache
docker-compose logs -f api-service      # Ver logs de un servicio
docker-compose restart data-collector   # Reiniciar servicio
docker-compose down                     # Bajar todos los servicios
docker-compose down -v                  # Bajar y eliminar volumes

# Builds
docker-compose build                    # Build de todas las images
docker-compose build --no-cache         # Build desde cero
docker-compose up --build               # Build y start

# Producción
docker-compose -f docker-compose.prod.yml up -d
docker-compose -f docker-compose.prod.yml ps
docker-compose -f docker-compose.prod.yml logs -f

# Troubleshooting
docker exec -it elproducto-postgres psql -U postgres -d elproducto
docker exec -it elproducto-redis redis-cli
docker exec -it elproducto-api sh
```

### Nginx Configuration

**`nginx/nginx.conf`** (desarrollo):

```nginx
events {
    worker_connections 1024;
}

http {
    upstream api_backend {
        server api-service:8080;
    }

    server {
        listen 80;
        server_name localhost;

        location /api/ {
            proxy_pass http://api_backend/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        location /actuator/ {
            proxy_pass http://api_backend/actuator/;
        }
    }
}
```

**`nginx/nginx.prod.conf`** (producción):

```nginx
events {
    worker_connections 2048;
}

http {
    # Rate limiting
    limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;

    upstream api_backend {
        least_conn;
        server api-service:8080 max_fails=3 fail_timeout=30s;
        # Si tienes múltiples instancias:
        # server api-service-2:8080 max_fails=3 fail_timeout=30s;
    }

    # Caché
    proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=api_cache:10m max_size=100m inactive=60m use_temp_path=off;

    server {
        listen 80;
        server_name your-domain.com;
        return 301 https://$server_name$request_uri;
    }

    server {
        listen 443 ssl http2;
        server_name your-domain.com;

        ssl_certificate /etc/nginx/ssl/cert.pem;
        ssl_certificate_key /etc/nginx/ssl/key.pem;
        ssl_protocols TLSv1.2 TLSv1.3;

        # Gzip
        gzip on;
        gzip_types application/json;

        location /api/ {
            limit_req zone=api_limit burst=20 nodelay;

            proxy_pass http://api_backend/api/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;

            # Timeouts
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;

            # Caché
            proxy_cache api_cache;
            proxy_cache_valid 200 5m;
            proxy_cache_use_stale error timeout http_500 http_502 http_503 http_504;
            add_header X-Cache-Status $upstream_cache_status;
        }

        location /actuator/health {
            proxy_pass http://api_backend/actuator/health;
            access_log off;
        }
    }
}
```

### CI/CD con Docker

**GitHub Actions** - `.github/workflows/docker-build.yml`:

```yaml
name: Docker Build and Push

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'

    - name: Build with Maven
      run: |
        cd data-collector-service
        mvn clean package -DskipTests

    - name: Log in to Docker Hub
      uses: docker/login-action@v2
      with:
        username: ${{ secrets.DOCKER_HUB_USERNAME }}
        password: ${{ secrets.DOCKER_HUB_TOKEN }}

    - name: Build and push Docker image
      uses: docker/build-push-action@v4
      with:
        context: ./data-collector-service
        push: true
        tags: |
          youruser/elproducto-collector:latest
          youruser/elproducto-collector:${{ github.sha }}

    - name: Build and push API service
      uses: docker/build-push-action@v4
      with:
        context: ./api-service
        push: true
        tags: |
          youruser/elproducto-api:latest
          youruser/elproducto-api:${{ github.sha }}
```

### Mejores Prácticas Docker

1. **Multi-stage builds** para reducir tamaño de imágenes finales
2. **Health checks** en todos los servicios
3. **No-root user** en containers
4. **Resource limits** en producción
5. **Secrets management** con Docker secrets o variables de entorno
6. **Logging** a stdout/stderr para que Docker lo capture
7. **Volumes** para datos persistentes
8. **Networks** aisladas para seguridad
9. **Restart policies** configuradas
10. **Image tagging** con versiones semánticas

---

## 6. Modelado de Datos

### Esquema de Base de Datos (PostgreSQL)

#### Tabla: `leagues` (Competiciones)

```sql
CREATE TABLE leagues (
    id BIGSERIAL PRIMARY KEY,
    external_id INTEGER UNIQUE NOT NULL,  -- ID de la API externa
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    logo_url VARCHAR(512),
    type VARCHAR(50),  -- 'League', 'Cup', 'International'
    current_season INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_leagues_external_id ON leagues(external_id);
CREATE INDEX idx_leagues_country ON leagues(country);
```

---

#### Tabla: `teams` (Equipos)

```sql
CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    external_id INTEGER UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    short_name VARCHAR(50),
    logo_url VARCHAR(512),
    country VARCHAR(100),
    founded_year INTEGER,
    venue_name VARCHAR(255),
    venue_city VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_teams_external_id ON teams(external_id);
CREATE INDEX idx_teams_name ON teams(name);
CREATE INDEX idx_teams_country ON teams(country);
```

---

#### Tabla: `matches` (Partidos)

```sql
CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    external_id INTEGER UNIQUE NOT NULL,
    league_id BIGINT REFERENCES leagues(id),
    season INTEGER NOT NULL,
    round VARCHAR(100),

    -- Equipos
    home_team_id BIGINT REFERENCES teams(id),
    away_team_id BIGINT REFERENCES teams(id),

    -- Fecha y estado
    match_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,  -- 'SCHEDULED', 'LIVE', 'FINISHED', 'CANCELLED'
    minute INTEGER,  -- Minuto del partido (si está en vivo)

    -- Resultado
    home_team_goals INTEGER,
    away_team_goals INTEGER,

    -- Penales (si aplica)
    home_team_penalty_goals INTEGER,
    away_team_penalty_goals INTEGER,

    -- Venue
    venue_name VARCHAR(255),
    venue_city VARCHAR(100),

    -- Árbitro
    referee VARCHAR(255),

    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_matches_external_id ON matches(external_id);
CREATE INDEX idx_matches_league_season ON matches(league_id, season);
CREATE INDEX idx_matches_date ON matches(match_date);
CREATE INDEX idx_matches_status ON matches(status);
CREATE INDEX idx_matches_home_team ON matches(home_team_id);
CREATE INDEX idx_matches_away_team ON matches(away_team_id);
CREATE INDEX idx_matches_date_status ON matches(match_date, status);
```

---

#### Tabla: `match_statistics` (Estadísticas del partido)

```sql
CREATE TABLE match_statistics (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT REFERENCES matches(id) ON DELETE CASCADE,
    team_id BIGINT REFERENCES teams(id),

    -- Estadísticas
    shots_on_goal INTEGER,
    shots_off_goal INTEGER,
    total_shots INTEGER,
    blocked_shots INTEGER,
    shots_inside_box INTEGER,
    shots_outside_box INTEGER,

    fouls INTEGER,
    corner_kicks INTEGER,
    offsides INTEGER,
    ball_possession INTEGER,  -- Porcentaje
    yellow_cards INTEGER,
    red_cards INTEGER,

    goalkeeper_saves INTEGER,
    total_passes INTEGER,
    passes_accurate INTEGER,
    passes_percentage INTEGER,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(match_id, team_id)
);

CREATE INDEX idx_match_stats_match ON match_statistics(match_id);
```

---

#### Tabla: `match_events` (Eventos del partido)

```sql
CREATE TABLE match_events (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT REFERENCES matches(id) ON DELETE CASCADE,
    team_id BIGINT REFERENCES teams(id),

    time_elapsed INTEGER NOT NULL,  -- Minuto
    time_extra INTEGER,  -- Tiempo adicional

    type VARCHAR(50) NOT NULL,  -- 'Goal', 'Card', 'Substitution'
    detail VARCHAR(100),  -- 'Normal Goal', 'Penalty', 'Yellow Card', etc

    player_name VARCHAR(255),
    player_external_id INTEGER,

    assist_player_name VARCHAR(255),
    assist_player_external_id INTEGER,

    -- Para sustituciones
    player_in_name VARCHAR(255),
    player_in_external_id INTEGER,
    player_out_name VARCHAR(255),
    player_out_external_id INTEGER,

    comments TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_match_events_match ON match_events(match_id);
CREATE INDEX idx_match_events_type ON match_events(type);
CREATE INDEX idx_match_events_time ON match_events(time_elapsed);
```

---

#### Tabla: `match_lineups` (Alineaciones)

```sql
CREATE TABLE match_lineups (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT REFERENCES matches(id) ON DELETE CASCADE,
    team_id BIGINT REFERENCES teams(id),

    player_name VARCHAR(255) NOT NULL,
    player_external_id INTEGER,
    player_number INTEGER,
    position VARCHAR(50),  -- 'G', 'D', 'M', 'F'

    is_starter BOOLEAN DEFAULT true,

    formation VARCHAR(20),  -- '4-4-2', '4-3-3', etc

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_lineups_match ON match_lineups(match_id);
CREATE INDEX idx_lineups_team ON match_lineups(team_id);
```

---

#### Tabla: `standings` (Tabla de posiciones)

```sql
CREATE TABLE standings (
    id BIGSERIAL PRIMARY KEY,
    league_id BIGINT REFERENCES leagues(id),
    season INTEGER NOT NULL,
    team_id BIGINT REFERENCES teams(id),

    rank INTEGER NOT NULL,
    points INTEGER NOT NULL,

    played INTEGER NOT NULL,
    wins INTEGER NOT NULL,
    draws INTEGER NOT NULL,
    losses INTEGER NOT NULL,

    goals_for INTEGER NOT NULL,
    goals_against INTEGER NOT NULL,
    goal_difference INTEGER NOT NULL,

    form VARCHAR(10),  -- 'WWDLL' últimos 5 partidos

    status VARCHAR(100),  -- 'Champions League', 'Relegation', etc
    description TEXT,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(league_id, season, team_id)
);

CREATE INDEX idx_standings_league_season ON standings(league_id, season);
CREATE INDEX idx_standings_rank ON standings(rank);
```

---

#### Tabla: `api_raw_data` (Datos crudos de API)

```sql
CREATE TABLE api_raw_data (
    id BIGSERIAL PRIMARY KEY,
    endpoint VARCHAR(255) NOT NULL,
    external_id INTEGER,
    request_params JSONB,
    response_data JSONB NOT NULL,
    status_code INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_raw_data_endpoint ON api_raw_data(endpoint);
CREATE INDEX idx_raw_data_external_id ON api_raw_data(external_id);
CREATE INDEX idx_raw_data_created ON api_raw_data(created_at);
```

---

### Migración Flyway - V1__initial_schema.sql

El archivo completo iría en `src/main/resources/db/migration/V1__initial_schema.sql` con todas las tablas anteriores.

---

## 6. Arquitectura de Microservicios

### Microservicio 1: data-collector-service

**Responsabilidad:** Consumir API externa y poblar base de datos

**Componentes:**

1. **API Client (Feign)**
```java
@FeignClient(name = "api-football", url = "${api.football.base-url}")
public interface ApiFootballClient {

    @GetMapping("/fixtures")
    FixturesResponse getFixtures(
        @RequestParam("league") Integer leagueId,
        @RequestParam("season") Integer season,
        @RequestParam("from") String fromDate,
        @RequestParam("to") String toDate
    );

    @GetMapping("/fixtures")
    FixtureDetailResponse getFixtureById(@RequestParam("id") Integer fixtureId);

    @GetMapping("/fixtures/statistics")
    StatisticsResponse getStatistics(@RequestParam("fixture") Integer fixtureId);

    // Más endpoints...
}
```

2. **Scheduler Job**
```java
@Component
public class MatchCollectionJob {

    @Scheduled(cron = "0 0 */6 * * *")  // Cada 6 horas
    public void collectFinishedMatches() {
        // Lógica de recolección
    }

    @Scheduled(fixedDelay = 120000)  // Cada 2 minutos
    public void collectLiveMatches() {
        // Lógica para partidos en vivo
    }
}
```

3. **Service Layer**
```java
@Service
public class CollectionService {

    public void collectLeagueMatches(Integer leagueId, Integer season) {
        // 1. Llamar API
        // 2. Transform DTOs → Entities
        // 3. Save to DB
        // 4. Save raw JSON
    }

    public void collectHistoricalData(Integer yearsBack) {
        // Popular datos históricos (3 años)
    }
}
```

---

### Microservicio 2: api-service

**Responsabilidad:** Exponer API REST para frontend

**Endpoints principales:**

```java
@RestController
@RequestMapping("/api/v1")
public class MatchController {

    @GetMapping("/matches")
    public Page<MatchDto> getMatches(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Long leagueId,
        @RequestParam(required = false) Long teamId,
        @RequestParam(required = false) String status,
        Pageable pageable
    ) {
        // ...
    }

    @GetMapping("/matches/{id}")
    @Cacheable("matches")
    public MatchDetailDto getMatch(@PathVariable Long id) {
        // ...
    }

    @GetMapping("/matches/live")
    @Cacheable(value = "live-matches", unless = "#result.isEmpty()")
    public List<MatchDto> getLiveMatches() {
        // ...
    }
}
```

**Configuración de Cache:**
```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
            "matches", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)),
            "live-matches", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30)),
            "standings", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
        );

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }
}
```

---

## 7. Plan de Implementación

### Fase 1: Setup Inicial (Semana 1-2)

**Tareas:**
1. ✅ Decidir API externa (API-Football)
2. ✅ Setup proyecto Spring Boot 3.2 con Java 21
3. ✅ Configurar PostgreSQL local
4. ✅ Configurar Redis local
5. ✅ Crear schema inicial con Flyway
6. ✅ Configurar Feign client para API
7. ✅ Tests de conexión a API

**Entregable:** Proyecto base funcional, conectado a DB y API

---

### Fase 2: Recolección Básica (Semana 3-4)

**Tareas:**
1. ✅ Implementar entities JPA básicas (Match, Team, League)
2. ✅ Implementar repositories
3. ✅ Implementar service de recolección básico
4. ✅ Popular ligas argentinas
5. ✅ Popular equipos argentinos
6. ✅ Recolectar partidos de temporada actual
7. ✅ Tests unitarios e integración

**Entregable:** Microservicio que puede recolectar partidos actuales

---

### Fase 3: Datos Históricos (Semana 5)

**Tareas:**
1. ✅ Script de población histórica (3 años)
2. ✅ Batch processing para múltiples temporadas
3. ✅ Rate limiting management
4. ✅ Error handling y retry logic
5. ✅ Logging detallado
6. ✅ Validación de datos

**Entregable:** Base de datos poblada con 3 años de datos

---

### Fase 4: Estadísticas y Eventos (Semana 6)

**Tareas:**
1. ✅ Implementar entities de statistics y events
2. ✅ Recolectar estadísticas de partidos
3. ✅ Recolectar eventos de partidos
4. ✅ Recolectar alineaciones
5. ✅ Actualizar partidos existentes con datos completos

**Entregable:** Datos completos de partidos con stats y eventos

---

### Fase 5: Scheduling y Automatización (Semana 7)

**Tareas:**
1. ✅ Configurar Quartz scheduler
2. ✅ Job para partidos finalizados (cada 6 horas)
3. ✅ Job para partidos en vivo (cada 2 minutos)
4. ✅ Job para standings (diario)
5. ✅ Distributed locking (ShedLock)
6. ✅ Monitoring de jobs

**Entregable:** Recolección automática funcionando 24/7

---

### Fase 6: API Service (Semana 8-9)

**Tareas:**
1. ✅ Crear proyecto api-service
2. ✅ Implementar controllers REST
3. ✅ Implementar DTOs y mappers
4. ✅ Configurar cache con Redis
5. ✅ Implementar paginación
6. ✅ Implementar filtros y búsqueda
7. ✅ Documentación Swagger
8. ✅ Tests de API

**Entregable:** API REST funcional y documentada

---

### Fase 7: Optimización y Testing (Semana 10)

**Tareas:**
1. ✅ Optimización de queries
2. ✅ Indices adicionales
3. ✅ Cache tuning
4. ✅ Performance testing
5. ✅ Security hardening
6. ✅ Logging y monitoring
7. ✅ Documentación completa

**Entregable:** Sistema optimizado y listo para producción

---

### Fase 8: Deploy (Semana 11)

**Tareas:**
1. ✅ Setup VPS (DigitalOcean/Hetzner)
2. ✅ Deploy PostgreSQL
3. ✅ Deploy Redis
4. ✅ Deploy microservicios
5. ✅ Configurar Nginx
6. ✅ SSL certificates
7. ✅ Monitoring setup
8. ✅ Backups automáticos

**Entregable:** Sistema en producción

---

## Checklist de Decisiones

- [ ] **API Externa:** API-Football (Basic plan - $15/mes)
- [ ] **Base de Datos:** PostgreSQL 16
- [ ] **Cache:** Redis 7
- [ ] **Cloud Provider:** DigitalOcean (para futuro)
- [ ] **Java:** Java 21 LTS
- [ ] **Framework:** Spring Boot 3.2.x
- [ ] **Build Tool:** Maven (o Gradle según preferencia)
- [ ] **HTTP Client:** OpenFeign o WebClient
- [ ] **Migrations:** Flyway
- [ ] **Scheduler:** Quartz
- [ ] **Logging:** Logback + Slf4j
- [ ] **Metrics:** Micrometer + Prometheus
- [ ] **Documentation:** Swagger/OpenAPI 3

---

## Próximos Pasos Inmediatos

1. **Crear cuenta en API-Football** y obtener API key
2. **Setup proyecto Spring Boot** con las dependencias listadas
3. **Configurar PostgreSQL** local
4. **Crear primer migration** con tablas básicas
5. **Implementar primer Feign client** para probar conexión a API
6. **Popular tabla de ligas** argentinas

---

## Recursos y Referencias

- **API-Football Docs:** https://www.api-football.com/documentation-v3
- **Spring Boot Docs:** https://docs.spring.io/spring-boot/docs/current/reference/html/
- **Spring Native:** https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/
- **Flyway:** https://flywaydb.org/documentation/
- **PostgreSQL:** https://www.postgresql.org/docs/