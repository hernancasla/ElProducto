# Multi-stage Dockerfile para compilación GraalVM Native Image

# Stage 1: Build con GraalVM
FROM ghcr.io/graalvm/native-image:ol9-java21-22.3.3 AS builder

WORKDIR /build

# Copiar archivos de configuración Maven
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Descargar dependencias (layer cache)
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY src src

# Compilar a Native Image
RUN ./mvnw -Pnative native:compile -DskipTests

# Stage 2: Runtime mínimo
FROM gcr.io/distroless/base-debian11

WORKDIR /app

# Copiar binario nativo desde el builder
COPY --from=builder /build/target/data-collector-service .

# Usuario no privilegiado
USER nonroot:nonroot

# Exponer puerto
EXPOSE 8081

# Comando de inicio
ENTRYPOINT ["/app/data-collector-service"]