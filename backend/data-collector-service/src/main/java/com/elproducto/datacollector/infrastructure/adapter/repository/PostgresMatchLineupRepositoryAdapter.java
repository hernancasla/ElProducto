package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.MatchLineup;
import com.elproducto.datacollector.domain.port.MatchLineupRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.MatchLineupEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Adaptador de repositorio para PostgreSQL usando R2DBC
 * Implementa el puerto MatchLineupRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresMatchLineupRepositoryAdapter implements MatchLineupRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresMatchLineupRepositoryAdapter.class);

    private final MatchLineupR2dbcRepository lineupRepository;

    public PostgresMatchLineupRepositoryAdapter(MatchLineupR2dbcRepository lineupRepository) {
        this.lineupRepository = lineupRepository;
    }

    @Override
    public Mono<List<MatchLineup>> saveAll(List<MatchLineup> lineups) {
        logger.info("[POSTGRES] Guardando {} alineaciones en PostgreSQL", lineups.size());

        // Para alineaciones, primero eliminamos las existentes del partido y luego insertamos las nuevas
        // Esto garantiza que siempre tengamos la lista completa actualizada
        if (lineups.isEmpty()) {
            return Mono.just(List.of());
        }

        Long fixtureId = lineups.get(0).fixtureId();

        return lineupRepository.deleteByFixtureId(fixtureId)
                .then(Flux.fromIterable(lineups)
                        .map(MatchLineupEntity::fromDomain)
                        .collectList()
                        .flatMap(entities -> lineupRepository.saveAll(entities).collectList())
                )
                .map(savedEntities -> savedEntities.stream()
                        .map(MatchLineupEntity::toDomain)
                        .toList()
                )
                .doOnSuccess(savedLineups ->
                    logger.info("[POSTGRES] {} alineaciones guardadas exitosamente para partido {}", savedLineups.size(), fixtureId)
                )
                .doOnError(error ->
                    logger.error("[POSTGRES] Error guardando alineaciones", error)
                );
    }

    @Override
    public Flux<MatchLineup> findAll() {
        logger.info("[POSTGRES] Recuperando todas las alineaciones");

        return lineupRepository.findAll()
                .map(MatchLineupEntity::toDomain)
                .doOnComplete(() -> logger.info("[POSTGRES] Recuperacion de alineaciones completada"))
                .doOnError(error -> logger.error("[POSTGRES] Error recuperando alineaciones", error));
    }

    @Override
    public Flux<MatchLineup> findByFixtureId(Long fixtureId) {
        logger.info("[POSTGRES] Recuperando alineaciones del partido {}", fixtureId);

        return lineupRepository.findByFixtureId(fixtureId)
                .map(MatchLineupEntity::toDomain)
                .doOnComplete(() -> logger.info("[POSTGRES] Recuperacion de alineaciones completada"))
                .doOnError(error -> logger.error("[POSTGRES] Error recuperando alineaciones por partido", error));
    }

    @Override
    public Flux<MatchLineup> findByFixtureIdAndTeamId(Long fixtureId, Long teamId) {
        logger.info("[POSTGRES] Recuperando alineacion del equipo {} en partido {}", teamId, fixtureId);

        return lineupRepository.findByFixtureIdAndTeamId(fixtureId, teamId)
                .map(MatchLineupEntity::toDomain)
                .doOnComplete(() -> logger.info("[POSTGRES] Recuperacion de alineacion completada"))
                .doOnError(error -> logger.error("[POSTGRES] Error recuperando alineacion por equipo", error));
    }

    @Override
    public Flux<MatchLineup> findStartersByFixtureIdAndTeamId(Long fixtureId, Long teamId) {
        logger.info("[POSTGRES] Recuperando titulares del equipo {} en partido {}", teamId, fixtureId);

        return lineupRepository.findByFixtureIdAndTeamIdAndIsStarter(fixtureId, teamId, true)
                .map(MatchLineupEntity::toDomain)
                .doOnComplete(() -> logger.info("[POSTGRES] Recuperacion de titulares completada"))
                .doOnError(error -> logger.error("[POSTGRES] Error recuperando titulares", error));
    }

    @Override
    public Flux<MatchLineup> findSubstitutesByFixtureIdAndTeamId(Long fixtureId, Long teamId) {
        logger.info("[POSTGRES] Recuperando suplentes del equipo {} en partido {}", teamId, fixtureId);

        return lineupRepository.findByFixtureIdAndTeamIdAndIsStarter(fixtureId, teamId, false)
                .map(MatchLineupEntity::toDomain)
                .doOnComplete(() -> logger.info("[POSTGRES] Recuperacion de suplentes completada"))
                .doOnError(error -> logger.error("[POSTGRES] Error recuperando suplentes", error));
    }

    @Override
    public Mono<Void> deleteByFixtureId(Long fixtureId) {
        logger.info("[POSTGRES] Eliminando alineaciones del partido {}", fixtureId);

        return lineupRepository.deleteByFixtureId(fixtureId)
                .doOnSuccess(v -> logger.info("[POSTGRES] Alineaciones eliminadas exitosamente"))
                .doOnError(error -> logger.error("[POSTGRES] Error eliminando alineaciones", error));
    }

    @Override
    public Mono<Boolean> existsByFixtureId(Long fixtureId) {
        return lineupRepository.existsByFixtureId(fixtureId);
    }

    @Override
    public Mono<Long> countByFixtureId(Long fixtureId) {
        return lineupRepository.countByFixtureId(fixtureId);
    }
}