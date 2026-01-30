package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.FixtureStatistics;
import com.elproducto.datacollector.domain.port.FixtureStatisticsRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.FixtureStatisticsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Adaptador de repositorio para PostgreSQL usando R2DBC
 * Implementa el puerto FixtureStatisticsRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresFixtureStatisticsRepositoryAdapter implements FixtureStatisticsRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresFixtureStatisticsRepositoryAdapter.class);

    private final FixtureStatisticsR2dbcRepository statisticsRepository;

    public PostgresFixtureStatisticsRepositoryAdapter(FixtureStatisticsR2dbcRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public Mono<List<FixtureStatistics>> saveAll(List<FixtureStatistics> statistics) {
        logger.info("💾 [POSTGRES] Guardando {} estadísticas en PostgreSQL", statistics.size());

        return Flux.fromIterable(statistics)
                .flatMap(stat -> {
                    // Validar que la estadística tenga los datos mínimos requeridos
                    if (stat.teamId() == null || stat.teamId() <= 0) {
                        logger.warn("⚠️ [POSTGRES] Estadística sin team ID válido, será omitida");
                        return Mono.empty();
                    }

                    return saveStatistic(stat);
                })
                .map(FixtureStatisticsEntity::toDomain)
                .collectList()
                .doOnSuccess(savedStats ->
                    logger.info("💾 [POSTGRES] {} estadísticas guardadas exitosamente", savedStats.size())
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando estadísticas", error)
                );
    }

    /**
     * Guarda o actualiza una estadística en la base de datos
     * @param statistics Estadística del dominio
     * @return Mono con la entidad guardada
     */
    private Mono<FixtureStatisticsEntity> saveStatistic(FixtureStatistics statistics) {
        return statisticsRepository.findByFixtureIdAndTeamId(statistics.fixtureId(), statistics.teamId())
            .doOnNext(existing -> {
                existing.markAsNotNew();
                logger.debug("💾 [POSTGRES] Estadística para equipo {} en partido {} ya existe, actualizando...",
                    statistics.teamId(), statistics.fixtureId());
            })
            .flatMap(existing -> {
                // Actualizar estadística existente
                existing.updateFrom(statistics);
                return statisticsRepository.save(existing);
            })
            .switchIfEmpty(
                // Crear nueva estadística
                Mono.defer(() -> {
                    logger.debug("💾 [POSTGRES] Estadística para equipo {} es nueva, creando...", statistics.teamId());
                    return statisticsRepository.save(FixtureStatisticsEntity.fromDomain(statistics));
                })
            );
    }

    @Override
    public Flux<FixtureStatistics> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todas las estadísticas");

        return statisticsRepository.findAll()
                .map(FixtureStatisticsEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de estadísticas completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando estadísticas", error));
    }

    @Override
    public Flux<FixtureStatistics> findByFixtureId(Long fixtureId) {
        logger.info("💾 [POSTGRES] Recuperando estadísticas del partido {}", fixtureId);

        return statisticsRepository.findByFixtureId(fixtureId)
                .map(FixtureStatisticsEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de estadísticas completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando estadísticas por partido", error));
    }

    @Override
    public Mono<FixtureStatistics> findByFixtureIdAndTeamId(Long fixtureId, Long teamId) {
        logger.info("💾 [POSTGRES] Buscando estadísticas del equipo {} en partido {}", teamId, fixtureId);

        return statisticsRepository.findByFixtureIdAndTeamId(fixtureId, teamId)
                .map(FixtureStatisticsEntity::toDomain)
                .doOnSuccess(stat -> {
                    if (stat != null) {
                        logger.info("💾 [POSTGRES] Estadística encontrada: {}", stat);
                    } else {
                        logger.info("💾 [POSTGRES] Estadística no encontrada");
                    }
                })
                .doOnError(error -> logger.error("❌ [POSTGRES] Error buscando estadística", error));
    }

    @Override
    public Flux<FixtureStatistics> findByTeamId(Long teamId) {
        logger.info("💾 [POSTGRES] Recuperando estadísticas del equipo: {}", teamId);

        return statisticsRepository.findByTeamId(teamId)
                .map(FixtureStatisticsEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de estadísticas completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando estadísticas por equipo", error));
    }

    @Override
    public Mono<Void> deleteByFixtureId(Long fixtureId) {
        logger.info("💾 [POSTGRES] Eliminando estadísticas del partido {}", fixtureId);

        return statisticsRepository.deleteByFixtureId(fixtureId)
                .doOnSuccess(v -> logger.info("💾 [POSTGRES] Estadísticas eliminadas exitosamente"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error eliminando estadísticas", error));
    }

    @Override
    public Mono<Boolean> existsByFixtureId(Long fixtureId) {
        return statisticsRepository.existsByFixtureId(fixtureId);
    }
}