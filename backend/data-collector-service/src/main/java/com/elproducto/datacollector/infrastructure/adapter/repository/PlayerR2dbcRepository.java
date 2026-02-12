package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.PlayerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla players
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface PlayerR2dbcRepository extends ReactiveCrudRepository<PlayerEntity, Long> {

    /**
     * Busca un jugador por su ID de API-Football
     * @param apiFootballId ID del jugador en API-Football
     * @return Mono con la entidad del jugador si existe
     */
    Mono<PlayerEntity> findByApiFootballId(Long apiFootballId);

    /**
     * Busca jugadores por ID de equipo (API-Football ID)
     * @param teamId ID del equipo en API-Football
     * @return Flux con los jugadores del equipo
     */
    Flux<PlayerEntity> findByTeamId(Long teamId);

    /**
     * Busca jugadores por nacionalidad
     * @param nationality Nacionalidad del jugador
     * @return Flux con los jugadores de esa nacionalidad
     */
    Flux<PlayerEntity> findByNationality(String nationality);

    /**
     * Busca un jugador por su nombre
     * @param name Nombre del jugador
     * @return Flux con los jugadores que coinciden con el nombre
     */
    Flux<PlayerEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Elimina un jugador por su ID de API-Football
     * @param apiFootballId ID del jugador en API-Football
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByApiFootballId(Long apiFootballId);

    /**
     * Verifica si existe un jugador con el ID de API-Football dado
     * @param apiFootballId ID del jugador en API-Football
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsByApiFootballId(Long apiFootballId);
}