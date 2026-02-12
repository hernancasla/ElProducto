# Scripts

Este directorio contiene scripts útiles para desarrollo, deployment y mantenimiento del proyecto.

## Scripts Disponibles

### Desarrollo

#### `dev-start.sh`
Inicia el entorno de desarrollo completo

```bash
./scripts/dev-start.sh
```

#### `dev-stop.sh`
Detiene todos los servicios de desarrollo

```bash
./scripts/dev-stop.sh
```

#### `dev-reset.sh`
Reset completo del entorno de desarrollo (¡CUIDADO! Borra datos)

```bash
./scripts/dev-reset.sh
```

---

### Base de Datos

#### `db-backup.sh`
Crea un backup de la base de datos

```bash
./scripts/db-backup.sh [filename]
```

#### `db-restore.sh`
Restaura un backup de la base de datos

```bash
./scripts/db-restore.sh <filename>
```

#### `db-migrate.sh`
Ejecuta migrations de Flyway

```bash
./scripts/db-migrate.sh
```

#### `db-seed.sh`
Puebla la base de datos con datos de prueba

```bash
./scripts/db-seed.sh
```

---

### Build y Deploy

#### `build-all.sh`
Hace build de todos los microservicios

```bash
./scripts/build-all.sh
```

#### `docker-build.sh`
Construye todas las imágenes Docker

```bash
./scripts/docker-build.sh [version]
```

#### `docker-push.sh`
Push de imágenes Docker al registry

```bash
./scripts/docker-push.sh [version]
```

#### `deploy-prod.sh`
Deploy a producción (con validaciones)

```bash
./scripts/deploy-prod.sh
```

---

### Utilidades

#### `populate-historical.sh`
Puebla la base de datos con datos históricos (3 años)

```bash
./scripts/populate-historical.sh
```

#### `check-health.sh`
Verifica el health de todos los servicios

```bash
./scripts/check-health.sh
```

#### `logs.sh`
Muestra logs de servicios

```bash
./scripts/logs.sh [service-name]
```

#### `clean.sh`
Limpia archivos temporales y build artifacts

```bash
./scripts/clean.sh
```

---

## Convenciones

- Todos los scripts deben ser ejecutables: `chmod +x scripts/*.sh`
- Todos los scripts deben tener manejo de errores (`set -e`)
- Todos los scripts deben tener documentación al inicio
- Usar variables de entorno cuando sea posible

## Próximos Pasos

1. Crear scripts básicos de desarrollo
2. Crear scripts de backup/restore
3. Crear scripts de deployment
4. Documentar cada script adecuadamente