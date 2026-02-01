package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.EntityType;
import com.elproducto.datacollector.domain.model.SyncMetadata;
import com.elproducto.datacollector.domain.model.SyncStatus;
import com.elproducto.datacollector.domain.port.SyncMetadataRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.SyncMetadataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Adaptador PostgreSQL para SyncMetadataRepository
 */
@Repository
@Primary
public class PostgresSyncMetadataRepositoryAdapter implements SyncMetadataRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresSyncMetadataRepositoryAdapter.class);

    private final SyncMetadataR2dbcRepository repository;

    public PostgresSyncMetadataRepositoryAdapter(SyncMetadataR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<SyncMetadata> save(SyncMetadata metadata) {
        logger.debug("[SYNC] Guardando metadata: {}", metadata);

        SyncMetadataEntity entity = SyncMetadataEntity.fromDomain(metadata);
        return repository.save(entity)
                .map(SyncMetadataEntity::toDomain)
                .doOnSuccess(saved -> logger.info("[SYNC] Metadata guardada: id={}, entity={}, status={}",
                        saved.id(), saved.entityType(), saved.status()))
                .doOnError(error -> logger.error("[SYNC] Error guardando metadata", error));
    }

    @Override
    public Mono<SyncMetadata> findById(Long id) {
        return repository.findById(id)
                .map(SyncMetadataEntity::toDomain);
    }

    @Override
    public Mono<SyncMetadata> findLastSuccessful(EntityType entityType) {
        logger.debug("[SYNC] Buscando ultima sync exitosa para: {}", entityType);

        return repository.findLastSuccessful(entityType.name())
                .map(SyncMetadataEntity::toDomain)
                .doOnSuccess(meta -> {
                    if (meta != null) {
                        logger.debug("[SYNC] Ultima sync exitosa: {} - {}", entityType, meta.completedAt());
                    } else {
                        logger.debug("[SYNC] No hay sync previa para: {}", entityType);
                    }
                });
    }

    @Override
    public Mono<SyncMetadata> findLastSuccessful(EntityType entityType, String scope) {
        return repository.findLastSuccessfulByScope(entityType.name(), scope)
                .map(SyncMetadataEntity::toDomain);
    }

    @Override
    public Mono<Boolean> isRunning(EntityType entityType, String scope) {
        return repository.existsRunning(entityType.name(), scope)
                .doOnSuccess(running -> {
                    if (running) {
                        logger.warn("[SYNC] Ya hay una sync en ejecucion para: {} scope={}", entityType, scope);
                    }
                });
    }

    @Override
    public Flux<SyncMetadata> findRunning() {
        return repository.findBySyncStatus(SyncStatus.RUNNING.name())
                .map(SyncMetadataEntity::toDomain);
    }

    @Override
    public Flux<SyncMetadata> findByEntityType(EntityType entityType, int limit) {
        return repository.findByEntityTypeOrderByCreatedAtDesc(entityType.name(), limit)
                .map(SyncMetadataEntity::toDomain);
    }

    @Override
    public Flux<SyncMetadata> findFailedForRetry(int maxRetries) {
        return repository.findFailedForRetry(maxRetries)
                .map(SyncMetadataEntity::toDomain);
    }

    @Override
    public Mono<Integer> cancelStale(LocalDateTime before) {
        logger.info("[SYNC] Cancelando syncs antiguas antes de: {}", before);

        return repository.cancelStale(before)
                .doOnSuccess(count -> {
                    if (count > 0) {
                        logger.warn("[SYNC] {} sincronizaciones canceladas por timeout", count);
                    }
                });
    }

    @Override
    public Mono<SyncStats> getStats(EntityType entityType, LocalDateTime since) {
        String entityName = entityType.name();

        return Mono.zip(
                repository.countByEntityTypeAndStatusSince(entityName, "SUCCESS", since).defaultIfEmpty(0L),
                repository.countByEntityTypeAndStatusSince(entityName, "FAILED", since).defaultIfEmpty(0L),
                repository.sumRecordsProcessed(entityName, since).defaultIfEmpty(0L),
                repository.sumApiCalls(entityName, since).defaultIfEmpty(0L),
                repository.avgDurationSeconds(entityName, since).defaultIfEmpty(0.0)
        ).map(tuple -> new SyncStats(
                entityType,
                tuple.getT1() + tuple.getT2(),  // total
                tuple.getT1(),                   // successful
                tuple.getT2(),                   // failed
                tuple.getT3(),                   // records
                tuple.getT4(),                   // api calls
                tuple.getT5()                    // avg duration
        ));
    }
}