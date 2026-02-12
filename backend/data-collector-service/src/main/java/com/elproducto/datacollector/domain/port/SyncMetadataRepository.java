package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.EntityType;
import com.elproducto.datacollector.domain.model.SyncMetadata;
import com.elproducto.datacollector.domain.model.SyncStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Puerto de salida para el repositorio de metadatos de sincronizacion
 */
public interface SyncMetadataRepository {

    /**
     * Guarda un registro de sincronizacion
     */
    Mono<SyncMetadata> save(SyncMetadata metadata);

    /**
     * Busca un registro por ID
     */
    Mono<SyncMetadata> findById(Long id);

    /**
     * Obtiene la ultima sincronizacion exitosa para una entidad
     */
    Mono<SyncMetadata> findLastSuccessful(EntityType entityType);

    /**
     * Obtiene la ultima sincronizacion exitosa para una entidad y scope
     */
    Mono<SyncMetadata> findLastSuccessful(EntityType entityType, String scope);

    /**
     * Verifica si hay una sincronizacion en ejecucion para la entidad
     */
    Mono<Boolean> isRunning(EntityType entityType, String scope);

    /**
     * Obtiene sincronizaciones en ejecucion
     */
    Flux<SyncMetadata> findRunning();

    /**
     * Obtiene historial de sincronizaciones para una entidad
     */
    Flux<SyncMetadata> findByEntityType(EntityType entityType, int limit);

    /**
     * Obtiene sincronizaciones fallidas pendientes de reintento
     */
    Flux<SyncMetadata> findFailedForRetry(int maxRetries);

    /**
     * Cancela sincronizaciones que excedieron el timeout
     */
    Mono<Integer> cancelStale(LocalDateTime before);

    /**
     * Obtiene estadisticas de sincronizacion por entidad
     */
    Mono<SyncStats> getStats(EntityType entityType, LocalDateTime since);

    /**
     * Estadisticas de sincronizacion
     */
    record SyncStats(
        EntityType entityType,
        long totalSyncs,
        long successfulSyncs,
        long failedSyncs,
        long totalRecordsProcessed,
        long totalApiCalls,
        double avgDurationSeconds
    ) {}
}