package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.MatchLineupEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla match_lineups
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface MatchLineupR2dbcRepository extends ReactiveCrudRepository<MatchLineupEntity, Long> {

    /**
     * Busca alineaciones por ID de partido
     * @param fixtureId ID del partido
     * @return Flux con las alineaciones del partido
     */
    Flux<MatchLineupEntity> findByFixtureId(Long fixtureId);

    /**
     * Busca alineaciones por ID de partido y equipo
     * @param fixtureId ID del partido
     * @param teamId ID del equipo
     * @return Flux con las alineaciones del equipo en ese partido
     */
    Flux<MatchLineupEntity> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca titulares de un partido
     * @param fixtureId ID del partido
     * @param isStarter true para titulares
     * @return Flux con los titulares del partido
     */
    Flux<MatchLineupEntity> findByFixtureIdAndIsStarter(Long fixtureId, boolean isStarter);

    /**
     * Busca titulares de un equipo en un partido
     * @param fixtureId ID del partido
     * @param teamId ID del equipo
     * @param isStarter true para titulares
     * @return Flux con los titulares del equipo
     */
    Flux<MatchLineupEntity> findByFixtureIdAndTeamIdAndIsStarter(Long fixtureId, Long teamId, boolean isStarter);

    /**
     * Busca jugadores por posición en un partido
     * @param fixtureId ID del partido
     * @param position Posición (G, D, M, F)
     * @return Flux con los jugadores en esa posición
     */
    Flux<MatchLineupEntity> findByFixtureIdAndPosition(Long fixtureId, String position);

    /**
     * Elimina las alineaciones de un partido
     * @param fixtureId ID del partido
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByFixtureId(Long fixtureId);

    /**
     * Verifica si existen alineaciones para un partido
     * @param fixtureId ID del partido
     * @return Mono con true si existen alineaciones
     */
    Mono<Boolean> existsByFixtureId(Long fixtureId);

    /**
     * Cuenta las alineaciones de un partido
     * @param fixtureId ID del partido
     * @return Mono con el número de jugadores en alineaciones
     */
    Mono<Long> countByFixtureId(Long fixtureId);

    /**
     * Busca un jugador específico en la alineación de un partido
     * @param fixtureId ID del partido
     * @param playerId ID del jugador
     * @return Mono con la alineación del jugador
     */
    Mono<MatchLineupEntity> findByFixtureIdAndPlayerId(Long fixtureId, Long playerId);
}