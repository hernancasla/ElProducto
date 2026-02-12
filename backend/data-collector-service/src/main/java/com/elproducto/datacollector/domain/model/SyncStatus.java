package com.elproducto.datacollector.domain.model;

/**
 * Estados posibles de una sincronizacion
 */
public enum SyncStatus {
    PENDING,    // Pendiente de ejecucion
    RUNNING,    // En ejecucion
    SUCCESS,    // Completada exitosamente
    FAILED,     // Fallida
    CANCELLED   // Cancelada
}