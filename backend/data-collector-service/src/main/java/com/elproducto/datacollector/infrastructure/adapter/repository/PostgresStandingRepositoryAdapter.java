package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.Standing;
import com.elproducto.datacollector.domain.port.StandingRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.StandingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Adaptador de repositorio para PostgreSQL usando R2DBC
 * Implementa el puerto StandingRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresStandingRepositoryAdapter implements StandingRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresStandingRepositoryAdapter.class);

    private final StandingR2dbcRepository standingRepository;

    public PostgresStandingRepositoryAdapter(StandingR2dbcRepository standingRepository) {
        this.standingRepository = standingRepository;
    }

    @Override
    public Mono<List<Standing>> saveAll(List<Standing> standings) {
        logger.info("💾 [POSTGRES] Guardando {} posiciones en PostgreSQL", standings.size());

        return Flux.fromIterable(standings)
                .flatMap(standing -> {
                    // Validar que la posición tenga los datos mínimos requeridos
                    if (standing.teamId() == null || standing.teamId() <= 0) {
                        logger.warn("⚠️ [POSTGRES] Posición sin team ID válido, será omitida");
                        return Mono.empty();
                    }

                    return saveStanding(standing);
                })
                .map(StandingEntity::toDomain)
                .collectList()
                .doOnSuccess(savedStandings ->
                    logger.info("💾 [POSTGRES] {} posiciones guardadas exitosamente", savedStandings.size())
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando posiciones", error)
                );
    }

    /**
     * Guarda o actualiza una posición en la base de datos
     * @param standing Posición del dominio
     * @return Mono con la entidad de la posición guardada
     */
    private Mono<StandingEntity> saveStanding(Standing standing) {
        return standingRepository.findByLeagueIdAndLeagueSeasonAndTeamId(
                standing.leagueId(), standing.leagueSeason(), standing.teamId())
            .doOnNext(existing -> {
                existing.markAsNotNew();
                logger.debug("💾 [POSTGRES] Posición para equipo {} en liga {} temporada {} ya existe, actualizando...",
                    standing.teamId(), standing.leagueId(), standing.leagueSeason());
            })
            .flatMap(existing -> {
                // Actualizar posición existente
                existing.updateFrom(standing);
                return standingRepository.save(existing);
            })
            .switchIfEmpty(
                // Crear nueva posición
                Mono.defer(() -> {
                    logger.debug("💾 [POSTGRES] Posición para equipo {} es nueva, creando...", standing.teamId());
                    return standingRepository.save(StandingEntity.fromDomain(standing));
                })
            );
    }

    @Override
    public Flux<Standing> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todas las posiciones");

        return standingRepository.findAll()
                .map(StandingEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de posiciones completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando posiciones", error));
    }

    @Override
    public Flux<Standing> findByLeagueIdAndSeason(Long leagueId, Integer season) {
        logger.info("💾 [POSTGRES] Recuperando posiciones de la liga {} temporada {}", leagueId, season);

        return standingRepository.findByLeagueIdAndLeagueSeasonOrderByRankAsc(leagueId, season)
                .map(StandingEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de posiciones completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando posiciones por liga", error));
    }

    @Override
    public Mono<Standing> findByLeagueIdAndSeasonAndTeamId(Long leagueId, Integer season, Long teamId) {
        logger.info("💾 [POSTGRES] Buscando posición del equipo {} en liga {} temporada {}", teamId, leagueId, season);

        return standingRepository.findByLeagueIdAndLeagueSeasonAndTeamId(leagueId, season, teamId)
                .map(StandingEntity::toDomain)
                .doOnSuccess(standing -> {
                    if (standing != null) {
                        logger.info("💾 [POSTGRES] Posición encontrada: {}", standing);
                    } else {
                        logger.info("💾 [POSTGRES] Posición no encontrada");
                    }
                })
                .doOnError(error -> logger.error("❌ [POSTGRES] Error buscando posición", error));
    }

    @Override
    public Flux<Standing> findByTeamId(Long teamId) {
        logger.info("💾 [POSTGRES] Recuperando posiciones del equipo: {}", teamId);

        return standingRepository.findByTeamId(teamId)
                .map(StandingEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de posiciones completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando posiciones por equipo", error));
    }

    @Override
    public Mono<Void> deleteByLeagueIdAndSeason(Long leagueId, Integer season) {
        logger.info("💾 [POSTGRES] Eliminando posiciones de la liga {} temporada {}", leagueId, season);

        return standingRepository.deleteByLeagueIdAndLeagueSeason(leagueId, season)
                .doOnSuccess(v -> logger.info("💾 [POSTGRES] Posiciones eliminadas exitosamente"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error eliminando posiciones", error));
    }
}