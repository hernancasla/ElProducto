# Epic 8: Notificaciones Push

**Prioridad:** Post-MVP

## Descripción
Implementar sistema de notificaciones push para alertar a usuarios sobre eventos importantes de partidos y equipos que siguen.

## Objetivos
- Notificar inicio de partidos
- Notificar goles y eventos importantes
- Notificar resultados finales
- Permitir personalización de notificaciones

## Valor de Negocio
Aumenta retención y engagement. Trae usuarios de vuelta a la app en momentos clave.

---

## Features Principales

### Feature 8.1: Infraestructura de Notificaciones
- Integración con FCM (Firebase Cloud Messaging)
- APNs para iOS
- Web Push para navegadores
- Sistema de envío de notificaciones desde backend
- Gestión de tokens de dispositivos

**Estimación:** 21 puntos

---

### Feature 8.2: Tipos de Notificaciones
- Inicio de partido
- Goles
- Tarjetas rojas
- Fin de partido
- Cambios en tabla de posiciones (opcional)

**Estimación:** 13 puntos

---

### Feature 8.3: Preferencias de Usuario
- Seguir equipos favoritos
- Configurar qué eventos notificar
- Horarios de no molestar
- Activar/desactivar notificaciones por tipo

**Estimación:** 13 puntos

---

### Feature 8.4: Notificaciones Programadas
- Recordatorios pre-partido
- Resumen diario/semanal
- Notificaciones de próximos partidos

**Estimación:** 8 puntos

---

## Estimación Total: ~55 puntos

## Riesgos
- **Medio:** Usuarios deshabilitan notificaciones si son molestas
- **Medio:** Costos de envío de notificaciones a escala
- **Bajo:** Problemas con permisos de notificaciones