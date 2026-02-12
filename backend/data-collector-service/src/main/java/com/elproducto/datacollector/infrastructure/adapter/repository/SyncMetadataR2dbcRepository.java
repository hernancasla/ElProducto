package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.SyncMetadataEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repositorio R2DBC para la tabla sync_metadata
 */
@Repository
public interface SyncMetadataR2dbcRepository extends ReactiveCrudRepository<SyncMetadataEntity, Long> {

    /**
     * Busca la ultima sincronizacion exitosa para una entidad
     */
    @Query("""
        SELECT * FROM sync_metadata
        WHERE entity_type = :entityType
        AND sync_status = 'SUCCESS'
        ORDER BY completed_at DESC
        LIMIT 1
        """)
    Mono<SyncMetadataEntity> findLastSuccessful(String entityType);

    /**
     * Busca la ultima sincronizacion exitosa para una entidad y scope
     */
    @Query("""
        SELECT * FROM sync_metadata
        WHERE entity_type = :entityType
        AND (sync_scope = :scope OR (:scope IS NULL AND sync_scope IS NULL))
        AND sync_status = 'SUCCESS'
        ORDER BY completed_at DESC
        LIMIT 1
        """)
    Mono<SyncMetadataEntity> findLastSuccessfulByScope(String entityType, String scope);

    /**
     * Verifica si hay sincronizacion en ejecucion
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM sync_metadata
            WHERE entity_type = :entityType
            AND (sync_scope = :scope OR (:scope IS NULL AND sync_scope IS NULL))
            AND sync_status = 'RUNNING'
        )
        """)
    Mono<Boolean> existsRunning(String entityType, String scope);

    /**
     * Obtiene sincronizaciones en ejecucion
     */
    Flux<SyncMetadataEntity> findBySyncStatus(String syncStatus);

    /**
     * Historial por entidad
     */
    @Query("""
        SELECT * FROM sync_metadata
        WHERE entity_type = :entityType
        ORDER BY created_at DESC
        LIMIT :limit
        """)
    Flux<SyncMetadataEntity> findByEntityTypeOrderByCreatedAtDesc(String entityType, int limit);

    /**
     * Sincronizaciones fallidas para reintento
     */
    @Query("""
        SELECT * FROM sync_metadata
        WHERE sync_status = 'FAILED'
        AND retry_count < :maxRetries
        ORDER BY completed_at ASC
        """)
    Flux<SyncMetadataEntity> findFailedForRetry(int maxRetries);

    /**
     * Cancela sincronizaciones antiguas
     */
    @Modifying
    @Query("""
        UPDATE sync_metadata
        SET sync_status = 'CANCELLED',
            completed_at = CURRENT_TIMESTAMP,
            error_message = 'Cancelled due to timeout'
        WHERE sync_status = 'RUNNING'
        AND started_at < :before
        """)
    Mono<Integer> cancelStale(LocalDateTime before);

    /**
     * Cuenta sincronizaciones por estado
     */
    @Query("""
        SELECT COUNT(*) FROM sync_metadata
        WHERE entity_type = :entityType
        AND sync_status = :status
        AND created_at >= :since
        """)
    Mono<Long> countByEntityTypeAndStatusSince(String entityType, String status, LocalDateTime since);

    /**
     * Suma total de registros procesados
     */
    @Query("""
        SELECT COALESCE(SUM(records_processed), 0) FROM sync_metadata
        WHERE entity_type = :entityType
        AND sync_status = 'SUCCESS'
        AND created_at >= :since
        """)
    Mono<Long> sumRecordsProcessed(String entityType, LocalDateTime since);

    /**
     * Suma total de API calls
     */
    @Query("""
        SELECT COALESCE(SUM(api_calls_made), 0) FROM sync_metadata
        WHERE entity_type = :entityType
        AND sync_status = 'SUCCESS'
        AND created_at >= :since
        """)
    Mono<Long> sumApiCalls(String entityType, LocalDateTime since);

    /**
     * Promedio de duracion en segundos
     */
    @Query("""
        SELECT COALESCE(AVG(EXTRACT(EPOCH FROM (completed_at - started_at))), 0)
        FROM sync_metadata
        WHERE entity_type = :entityType
        AND sync_status = 'SUCCESS'
        AND created_at >= :since
        """)
    Mono<Double> avgDurationSeconds(String entityType, LocalDateTime since);
}