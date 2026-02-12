package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.FixtureStatisticsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla fixture_statistics
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface FixtureStatisticsR2dbcRepository extends ReactiveCrudRepository<FixtureStatisticsEntity, Long> {

    /**
     * Busca estadísticas por ID de partido
     * @param fixtureId ID del partido
     * @return Flux con las estadísticas del partido (una por equipo)
     */
    Flux<FixtureStatisticsEntity> findByFixtureId(Long fixtureId);

    /**
     * Busca estadísticas de un equipo en un partido específico
     * @param fixtureId ID del partido
     * @param teamId ID del equipo
     * @return Mono con las estadísticas del equipo en ese partido
     */
    Mono<FixtureStatisticsEntity> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca todas las estadísticas de un equipo
     * @param teamId ID del equipo
     * @return Flux con las estadísticas del equipo
     */
    Flux<FixtureStatisticsEntity> findByTeamId(Long teamId);

    /**
     * Elimina las estadísticas de un partido
     * @param fixtureId ID del partido
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByFixtureId(Long fixtureId);

    /**
     * Verifica si existen estadísticas para un partido
     * @param fixtureId ID del partido
     * @return Mono con true si existen estadísticas
     */
    Mono<Boolean> existsByFixtureId(Long fixtureId);
}