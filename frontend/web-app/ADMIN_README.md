# 🔐 Backoffice de Administración

Panel de control administrativo para gestionar migraciones, base de datos y logs del sistema.

## 🚀 Acceso

**URL:** `http://localhost:3000/admin`

### Credenciales por Defecto (Desarrollo)

⚠️ **IMPORTANTE: Cambiar en producción**

- Usuario: `admin`
- Contraseña: Se debe configurar (ver sección de configuración)

## 🔧 Configuración Inicial

### 1. Generar Hash de Contraseña

```bash
# Generar hash para tu contraseña
node scripts/generate-admin-password.js tu_contraseña_segura

# Copiar el hash generado
```

### 2. Configurar Variables de Entorno

Edita `.env.local`:

```bash
# Usuario de administrador
ADMIN_USERNAME=admin

# Hash de contraseña (generado con el script)
ADMIN_PASSWORD_HASH=$2a$10$tu_hash_generado_aqui

# Secret para sesiones (generar uno aleatorio de 32+ caracteres)
SESSION_SECRET=tu_secret_key_muy_seguro_de_al_menos_32_caracteres

# IPs permitidas para acceder al backoffice (separadas por coma)
# En desarrollo, dejar vacío permite localhost automáticamente
# En producción, DEBE configurarse con IPs específicas
ADMIN_IP_WHITELIST=192.168.1.100,203.0.113.50
```

### 3. Reiniciar el Servidor

```bash
npm run dev
```

## 🛡️ Seguridad

### Autenticación

- Sesiones con `iron-session` (cookies encriptadas HTTP-only)
- Contraseñas hasheadas con bcrypt (10 rounds)
- Expiración de sesión: 8 horas
- Logout automático al cerrar sesión

### IP Whitelisting

El backoffice implementa restricción por IP:

**Desarrollo:**
- Sin `ADMIN_IP_WHITELIST` configurada: permite `127.0.0.1`, `::1`, `localhost`
- Ideal para desarrollo local sin configuración adicional

**Producción:**
- `ADMIN_IP_WHITELIST` **REQUERIDA**
- Sin whitelist = **DENEGAR TODO** por seguridad
- Formato: `IP1,IP2,IP3` (separadas por coma)

**Obtener tu IP pública:**
```bash
curl ifconfig.me
```

### Middleware de Protección

Todas las rutas `/admin/*` (excepto `/admin/login`) están protegidas por:

1. ✅ Verificación de IP whitelist
2. ✅ Verificación de sesión activa
3. ✅ Validación de expiración de sesión
4. ✅ Redirección a login si no autorizado

## 📱 Funcionalidades

### 1. Dashboard (`/admin`)

- Resumen del estado del sistema
- Accesos rápidos a funciones principales
- Estado de servicios (Backend, DB, Redis)

### 2. Migraciones (`/admin/migrations`)

- Ver lista de migraciones disponibles
- Estado de ejecución (pendiente/completada/fallida)
- Ejecutar migraciones manualmente

**API Backend requerida:**
```
GET  /api/admin/migrations
POST /api/admin/migrations/{id}/execute
```

### 3. Base de Datos (`/admin/database`)

- Listar todas las tablas
- Ver cantidad de filas y tamaño
- Explorar datos de tablas

**API Backend requerida:**
```
GET /api/admin/database/tables
GET /api/admin/database/tables/{tableName}
```

### 4. Logs (`/admin/logs`)

- Ver logs del sistema en tiempo real
- Filtrar por nivel (INFO, WARN, ERROR)
- Buscar por servicio

**API Backend requerida:**
```
GET /api/admin/logs?level=ERROR&service=data-collector
```

## 🔗 Integración con Backend

El backoffice necesita que el backend Spring Boot exponga estos endpoints:

```java
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @GetMapping("/migrations")
    public ResponseEntity<List<Migration>> getMigrations() {
        // Retornar lista de migraciones con Flyway
    }
    
    @PostMapping("/migrations/{id}/execute")
    public ResponseEntity<Void> executeMigration(@PathVariable String id) {
        // Ejecutar migración específica
    }
    
    @GetMapping("/database/tables")
    public ResponseEntity<List<TableInfo>> getTables() {
        // Listar tablas de PostgreSQL
    }
    
    @GetMapping("/logs")
    public ResponseEntity<List<LogEntry>> getLogs() {
        // Retornar logs del sistema
    }
}
```

## 🚀 Deploy en Producción

### Checklist de Seguridad

- [ ] Cambiar `ADMIN_USERNAME` y `ADMIN_PASSWORD_HASH`
- [ ] Generar `SESSION_SECRET` aleatorio (32+ caracteres)
- [ ] Configurar `ADMIN_IP_WHITELIST` con IPs específicas
- [ ] Habilitar HTTPS en producción
- [ ] Proteger endpoints `/api/admin/**` en Spring Security

---

**Desarrollado para ElProducto** 🔐
