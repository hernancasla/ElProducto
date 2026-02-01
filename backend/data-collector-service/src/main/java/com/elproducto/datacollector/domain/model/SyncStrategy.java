package com.elproducto.datacollector.domain.model;

/**
 * Estrategias de sincronizacion disponibles
 */
public enum SyncStrategy {
    FULL_REFRESH,   // Borrar todo y reinsertar
    UPSERT,         // Insertar nuevos, actualizar existentes
    INCREMENTAL,    // Solo agregar nuevos (no modifica existentes)
    DELETE_INSERT   // Borrar por scope y reinsertar
}