# Backend Services

Este directorio contiene todos los microservicios del backend de ElProducto.

## ⚡ GraalVM Native Image

**IMPORTANTE:** Todos los microservicios están diseñados para ser compilados a **GraalVM Native Image**.

### Beneficios de GraalVM Native
- ⚡ Tiempo de inicio instantáneo (~0.1s vs ~10s en JVM)
- 💾 Consumo de memoria reducido (~70% menos)
- 📦 Binarios autocontenidos (no requiere JVM instalado)
- 🚀 Performance optimizado para producción

### Requisitos
- **GraalVM 21** (Java 21 con Native Image)
- Todas las librerías deben ser compatibles con Native Image
- Configuración específica de hints y reflection

---

## Microservicios

### 1. data-collector-service
**Puerto:** 8081
**Descripción:** Microservicio encargado de recolectar datos de la API externa (API-Football) y almacenarlos en la base de datos.

**Responsabilidades:**
- Consumir API-Football
- Transformar y normalizar datos
- Almacenar datos en PostgreSQL
- Jobs automáticos de recolección
- Gestión de datos históricos (3 años)

**Tecnologías:**
- **GraalVM 21** (Native Image)
- Spring Boot 3.2.x
- Spring Data JPA
- WebClient (compatible con Native)
- Quartz Scheduler
- Flyway

**Compilación Native:**
```bash
./mvnw -Pnative native:compile
```

---

### 2. api-service
**Puerto:** 8080
**Descripción:** API REST que expone endpoints para ser consumidos por el frontend.

**Responsabilidades:**
- Exponer endpoints REST
- Implementar lógica de negocio
- Cache con Redis
- Paginación y filtros
- Documentación con Swagger

**Tecnologías:**
- **GraalVM 21** (Native Image)
- Spring Boot 3.2.x
- Spring Data JPA
- Spring Cache (Redis)
- Swagger/OpenAPI

**Compilación Native:**
```bash
./mvnw -Pnative native:compile
```

---

## Estructura Común

Cada microservicio sigue la siguiente estructura:

```
microservicio-name/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/elproducto/[service]/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── domain/
│   │   │       │   └── entity/
│   │   │       └── Application.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/
│   └── test/
├── Dockerfile
├── pom.xml
└── README.md
```

## Desarrollo Local

### Opción 1: Modo JVM (Desarrollo - más rápido)
```bash
# Iniciar solo PostgreSQL y Redis
cd ../..
docker-compose up -d postgres redis

# Ejecutar el microservicio en modo JVM
cd backend/data-collector-service
./mvnw spring-boot:run
```

### Opción 2: Modo Native (Testing - producción)
```bash
# Compilar a Native Image (tarda 2-5 minutos)
./mvnw -Pnative native:compile

# Ejecutar el binario nativo
./target/data-collector-service
```

### Opción 3: Con Docker
```bash
# Desde la raíz del proyecto
docker-compose up -d
```

## Build

### Build JVM (Desarrollo)
```bash
cd data-collector-service
./mvnw clean package
java -jar target/data-collector-service-0.0.1-SNAPSHOT.jar
```

### Build Native Image (Producción)
```bash
# Compilar a binario nativo
./mvnw -Pnative native:compile

# El binario estará en target/data-collector-service
./target/data-collector-service

# Ventajas:
# - Inicio instantáneo (~100ms)
# - Memoria reducida (~50-100MB vs 300-500MB JVM)
# - No requiere JVM instalado
```

### Build Docker con Native Image
```bash
cd data-collector-service
docker build -f Dockerfile.native -t elproducto-collector:native .

# O build multi-stage que compila dentro del container
docker build -t elproducto-collector:latest .
```

## GraalVM: Consideraciones Importantes

### ✅ Librerías Compatibles (Ya verificadas)
- Spring Boot 3.2.x
- Spring Data JPA
- PostgreSQL Driver
- Redis (Lettuce)
- Flyway
- WebClient
- Quartz Scheduler
- Micrometer
- Logback

### ⚠️ Evitar (No compatibles con Native)
- RestTemplate (usar WebClient)
- Reflection dinámica sin hints
- CGLIB proxies excesivos
- Lazy loading de Hibernate (configurar eager donde sea necesario)

### 📝 Best Practices para Native Image

1. **Usar WebClient en lugar de RestTemplate**
```java
// ❌ NO compatible
RestTemplate restTemplate = new RestTemplate();

// ✅ Compatible con Native
WebClient webClient = WebClient.builder().build();
```

2. **Usar Records para DTOs**
```java
// ✅ Óptimo para Native Image
public record MatchDto(Long id, String homeTeam, String awayTeam) {}
```

3. **Evitar reflection dinámica**
```java
// ❌ Puede causar problemas
Class.forName("com.example.SomeClass");

// ✅ Mejor
@RegisterReflectionForBinding(SomeClass.class)
```

4. **Configurar Hibernate correctamente**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none  # Usar Flyway para migrations
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
```

## Testing

```bash
# Tests unitarios
./mvnw test

# Tests de integración
./mvnw verify

# Coverage
./mvnw jacoco:report
```

## Próximos Pasos

1. Crear proyecto `data-collector-service`
2. Crear proyecto `api-service`
3. Implementar funcionalidad básica
4. Configurar CI/CD

Ver documentación completa en `/docs/epics/epic-01-technical-analysis.md`