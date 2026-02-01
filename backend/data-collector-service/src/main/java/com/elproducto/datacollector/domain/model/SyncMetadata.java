package com.elproducto.datacollector.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;

/**
 * Modelo de dominio para metadatos de sincronizacion
 * Registra el estado, progreso y resultados de cada sincronizacion
 */
public record SyncMetadata(
    Long id,
    EntityType entityType,
    String syncScope,           // null=global, "league:128", "fixture:123456"
    SyncStatus status,
    SyncStrategy strategy,
    LocalDateTime startedAt,
    LocalDateTime completedAt,
    // Metricas de registros
    int recordsProcessed,
    int recordsCreated,
    int recordsUpdated,
    int recordsDeleted,
    int recordsSkipped,
    // Metricas de API
    int apiCallsMade,
    int apiErrors,
    // Error handling
    String errorMessage,
    Map<String, Object> errorDetails,
    int retryCount,
    // Contexto
    SyncTrigger triggeredBy,
    String triggerEvent,
    Map<String, Object> parameters,
    LocalDateTime createdAt
) {
    /**
     * Crea una nueva sincronizacion en estado PENDING
     */
    public static SyncMetadata create(
            EntityType entityType,
            String syncScope,
            SyncStrategy strategy,
            SyncTrigger triggeredBy,
            String triggerEvent,
            Map<String, Object> parameters) {
        return new SyncMetadata(
            null,
            entityType,
            syncScope,
            SyncStatus.PENDING,
            strategy,
            null,
            null,
            0, 0, 0, 0, 0,
            0, 0,
            null, null, 0,
            triggeredBy,
            triggerEvent,
            parameters,
            LocalDateTime.now()
        );
    }

    /**
     * Marca la sincronizacion como iniciada
     */
    public SyncMetadata start() {
        return new SyncMetadata(
            id, entityType, syncScope,
            SyncStatus.RUNNING,
            strategy,
            LocalDateTime.now(),
            null,
            recordsProcessed, recordsCreated, recordsUpdated, recordsDeleted, recordsSkipped,
            apiCallsMade, apiErrors,
            errorMessage, errorDetails, retryCount,
            triggeredBy, triggerEvent, parameters, createdAt
        );
    }

    /**
     * Marca la sincronizacion como completada exitosamente
     */
    public SyncMetadata complete(int processed, int created, int updated, int deleted, int skipped, int apiCalls) {
        return new SyncMetadata(
            id, entityType, syncScope,
            SyncStatus.SUCCESS,
            strategy,
            startedAt,
            LocalDateTime.now(),
            processed, created, updated, deleted, skipped,
            apiCalls, apiErrors,
            null, null, retryCount,
            triggeredBy, triggerEvent, parameters, createdAt
        );
    }

    /**
     * Marca la sincronizacion como fallida
     */
    public SyncMetadata fail(String error, Map<String, Object> details) {
        return new SyncMetadata(
            id, entityType, syncScope,
            SyncStatus.FAILED,
            strategy,
            startedAt,
            LocalDateTime.now(),
            recordsProcessed, recordsCreated, recordsUpdated, recordsDeleted, recordsSkipped,
            apiCallsMade, apiErrors + 1,
            error, details, retryCount,
            triggeredBy, triggerEvent, parameters, createdAt
        );
    }

    /**
     * Incrementa el contador de llamadas API
     */
    public SyncMetadata incrementApiCalls() {
        return new SyncMetadata(
            id, entityType, syncScope, status, strategy,
            startedAt, completedAt,
            recordsProcessed, recordsCreated, recordsUpdated, recordsDeleted, recordsSkipped,
            apiCallsMade + 1, apiErrors,
            errorMessage, errorDetails, retryCount,
            triggeredBy, triggerEvent, parameters, createdAt
        );
    }

    /**
     * Calcula la duracion de la sincronizacion
     */
    public Duration getDuration() {
        if (startedAt == null) {
            return Duration.ZERO;
        }
        LocalDateTime end = completedAt != null ? completedAt : LocalDateTime.now();
        return Duration.between(startedAt, end);
    }

    /**
     * Verifica si la sincronizacion esta en progreso
     */
    public boolean isRunning() {
        return status == SyncStatus.RUNNING;
    }

    /**
     * Verifica si la sincronizacion fue exitosa
     */
    public boolean isSuccess() {
        return status == SyncStatus.SUCCESS;
    }

    /**
     * Verifica si la sincronizacion fallo
     */
    public boolean isFailed() {
        return status == SyncStatus.FAILED;
    }

    /**
     * Obtiene el total de registros afectados
     */
    public int getTotalAffected() {
        return recordsCreated + recordsUpdated + recordsDeleted;
    }

    @Override
    public String toString() {
        return "SyncMetadata{" +
                "id=" + id +
                ", entityType=" + entityType +
                ", scope=" + syncScope +
                ", status=" + status +
                ", processed=" + recordsProcessed +
                ", duration=" + getDuration().toSeconds() + "s" +
                '}';
    }
}