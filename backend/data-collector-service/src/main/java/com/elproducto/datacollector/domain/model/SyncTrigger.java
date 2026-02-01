package com.elproducto.datacollector.domain.model;

/**
 * Origen del trigger de sincronizacion
 */
public enum SyncTrigger {
    SCHEDULER,      // Disparado por cron/scheduler
    MANUAL,         // Disparado manualmente via API
    EVENT,          // Disparado por un evento (ej: fixture completed)
    DEPENDENCY,     // Disparado como dependencia de otra sync
    INITIALIZATION  // Disparado durante inicializacion del sistema
}