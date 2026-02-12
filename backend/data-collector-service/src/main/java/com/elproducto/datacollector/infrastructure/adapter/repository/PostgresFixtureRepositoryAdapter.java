package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.Fixture;
import com.elproducto.datacollector.domain.port.FixtureRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.FixtureEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adaptador de repositorio para PostgreSQL usando R2DBC
 * Implementa el puerto FixtureRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresFixtureRepositoryAdapter implements FixtureRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresFixtureRepositoryAdapter.class);

    private final FixtureR2dbcRepository fixtureRepository;

    public PostgresFixtureRepositoryAdapter(FixtureR2dbcRepository fixtureRepository) {
        this.fixtureRepository = fixtureRepository;
    }

    @Override
    public Mono<List<Fixture>> saveAll(List<Fixture> fixtures) {
        logger.info("💾 [POSTGRES] Guardando {} partidos en PostgreSQL", fixtures.size());

        return Flux.fromIterable(fixtures)
                .flatMap(fixture -> {
                    // Validar que el fixture tenga los datos mínimos requeridos
                    if (fixture.apiFootballId() == null || fixture.apiFootballId() <= 0) {
                        logger.warn("⚠️ [POSTGRES] Partido sin ID válido, será omitido");
                        return Mono.empty();
                    }

                    return saveFixture(fixture);
                })
                .map(FixtureEntity::toDomain)
                .collectList()
                .doOnSuccess(savedFixtures ->
                    logger.info("💾 [POSTGRES] {} partidos guardados exitosamente", savedFixtures.size())
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando partidos", error)
                );
    }

    /**
     * Guarda o actualiza un fixture en la base de datos
     * @param fixture Fixture del dominio
     * @return Mono con la entidad del fixture guardado
     */
    private Mono<FixtureEntity> saveFixture(Fixture fixture) {
        return fixtureRepository.findByApiFootballId(fixture.apiFootballId())
            .doOnNext(existing -> {
                existing.markAsNotNew();
                logger.debug("💾 [POSTGRES] Partido {} ya existe (ID: {}), actualizando...",
                    fixture.apiFootballId(), existing.getId());
            })
            .flatMap(existing -> {
                // Actualizar fixture existente
                existing.updateFrom(fixture);
                return fixtureRepository.save(existing);
            })
            .switchIfEmpty(
                // Crear nuevo fixture
                Mono.defer(() -> {
                    logger.debug("💾 [POSTGRES] Partido {} es nuevo, creando...", fixture.apiFootballId());
                    return fixtureRepository.save(FixtureEntity.fromDomain(fixture));
                })
            );
    }

    @Override
    public Flux<Fixture> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todos los partidos");

        return fixtureRepository.findAll()
                .map(FixtureEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de partidos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando partidos", error));
    }

    @Override
    public Flux<Fixture> findByLeagueIdAndSeason(Long leagueId, Integer season) {
        logger.info("💾 [POSTGRES] Recuperando partidos de la liga {} temporada {}", leagueId, season);

        return fixtureRepository.findByLeagueIdAndLeagueSeason(leagueId, season)
                .map(FixtureEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de partidos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando partidos por liga", error));
    }

    @Override
    public Flux<Fixture> findByTeamId(Long teamId) {
        logger.info("💾 [POSTGRES] Recuperando partidos del equipo: {}", teamId);

        return fixtureRepository.findByTeamId(teamId, teamId)
                .map(FixtureEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de partidos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando partidos por equipo", error));
    }

    @Override
    public Flux<Fixture> findByDateBetween(LocalDateTime from, LocalDateTime to) {
        logger.info("💾 [POSTGRES] Recuperando partidos entre {} y {}", from, to);

        return fixtureRepository.findByFixtureDateBetween(from, to)
                .map(FixtureEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de partidos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando partidos por fecha", error));
    }

    @Override
    public Mono<Fixture> findByApiFootballId(Long apiFootballId) {
        logger.info("💾 [POSTGRES] Buscando partido con API Football ID: {}", apiFootballId);

        return fixtureRepository.findByApiFootballId(apiFootballId)
                .map(FixtureEntity::toDomain)
                .doOnSuccess(fixture -> {
                    if (fixture != null) {
                        logger.info("💾 [POSTGRES] Partido encontrado: {}", fixture);
                    } else {
                        logger.info("💾 [POSTGRES] Partido no encontrado");
                    }
                })
                .doOnError(error -> logger.error("❌ [POSTGRES] Error buscando partido", error));
    }

    @Override
    public Flux<Fixture> findByStatus(String statusShort) {
        logger.info("💾 [POSTGRES] Recuperando partidos con estado: {}", statusShort);

        return fixtureRepository.findByStatusShort(statusShort)
                .map(FixtureEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de partidos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando partidos por estado", error));
    }

    @Override
    public Flux<Fixture> findByStatusShortIn(List<String> statuses) {
        logger.debug("[POSTGRES] Recuperando partidos con estados: {}", statuses);

        return fixtureRepository.findByStatusShortIn(statuses)
                .map(FixtureEntity::toDomain)
                .doOnComplete(() -> logger.debug("[POSTGRES] Recuperacion de partidos por estados completada"))
                .doOnError(error -> logger.error("[POSTGRES] Error recuperando partidos por estados", error));
    }
}