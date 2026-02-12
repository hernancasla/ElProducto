package com.elproducto.datacollector.domain.model;

/**
 * Frecuencia de sincronizacion para cada tipo de entidad
 */
public enum SyncFrequency {
    MANUAL,         // Solo sincronizacion manual
    HOURLY,         // Cada hora
    DAILY,          // Diario
    WEEKLY,         // Semanal
    EVENT_DRIVEN    // Disparado por eventos (ej: partido finalizado)
}