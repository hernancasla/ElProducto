# Infrastructure

Este directorio contiene toda la configuración de infraestructura para ElProducto.

## Contenido

### nginx/
Configuración de Nginx como reverse proxy

**Archivos:**
- `nginx.conf` - Configuración para desarrollo
- `nginx.prod.conf` - Configuración para producción
- `ssl/` - Certificados SSL (no commiteados)

### monitoring/
Configuración de monitoreo y observabilidad

**Componentes (Futuro):**
- Prometheus - Recolección de métricas
- Grafana - Visualización de métricas
- Loki - Logs centralizados
- Alertmanager - Gestión de alertas

### Terraform (Futuro)
Infrastructure as Code para deployment en cloud

### Kubernetes (Futuro)
Manifiestos de Kubernetes para escalamiento

---

## Nginx

### Desarrollo

El Nginx en desarrollo actúa como reverse proxy simple:

```nginx
http://localhost → nginx:80 → api-service:8080
```

### Producción

En producción, Nginx maneja:
- SSL/TLS termination
- Rate limiting
- Caching
- Load balancing (si hay múltiples instancias)
- Compresión gzip

---

## Monitoreo (Futuro)

### Prometheus
Recolecta métricas de:
- Spring Boot Actuator endpoints
- Métricas de sistema (CPU, RAM, disco)
- Métricas de PostgreSQL
- Métricas de Redis

### Grafana
Dashboards para visualizar:
- Performance de APIs
- Uso de recursos
- Errores y excepciones
- Tráfico de usuarios

### Loki
Agregación de logs de:
- Todos los microservicios
- Nginx access/error logs
- PostgreSQL logs

---

## Deployment

### Desarrollo Local
```bash
# Desde la raíz del proyecto
docker-compose up -d
```

### Producción (VPS)
```bash
docker-compose -f docker-compose.prod.yml up -d
```

### Cloud (DigitalOcean/Railway/Fly.io)
Ver documentación específica en `/docs/epics/epic-06-infrastructure.md`

---

## Próximos Pasos

1. Crear configuración básica de Nginx
2. Configurar SSL con Let's Encrypt
3. Implementar monitoreo con Prometheus + Grafana
4. Configurar backups automáticos de PostgreSQL
5. Implementar CI/CD pipeline

Ver documentación completa en `/docs/epics/epic-06-infrastructure.md`