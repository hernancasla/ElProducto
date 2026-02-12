package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.FixtureEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla fixture_events
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface FixtureEventR2dbcRepository extends ReactiveCrudRepository<FixtureEventEntity, Long> {

    /**
     * Busca eventos por ID de partido ordenados cronológicamente
     * @param fixtureId ID del partido
     * @return Flux con los eventos del partido
     */
    Flux<FixtureEventEntity> findByFixtureIdOrderByTimeElapsedAscTimeExtraAsc(Long fixtureId);

    /**
     * Busca eventos de un equipo en un partido específico
     * @param fixtureId ID del partido
     * @param teamId ID del equipo
     * @return Flux con los eventos del equipo en ese partido
     */
    Flux<FixtureEventEntity> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca eventos por tipo
     * @param fixtureId ID del partido
     * @param type Tipo de evento
     * @return Flux con los eventos del tipo especificado
     */
    Flux<FixtureEventEntity> findByFixtureIdAndType(Long fixtureId, String type);

    /**
     * Elimina los eventos de un partido
     * @param fixtureId ID del partido
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByFixtureId(Long fixtureId);

    /**
     * Verifica si existen eventos para un partido
     * @param fixtureId ID del partido
     * @return Mono con true si existen eventos
     */
    Mono<Boolean> existsByFixtureId(Long fixtureId);

    /**
     * Cuenta los eventos de un partido
     * @param fixtureId ID del partido
     * @return Mono con el número de eventos
     */
    Mono<Long> countByFixtureId(Long fixtureId);
}