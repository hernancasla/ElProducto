package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.StandingEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla standings
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface StandingR2dbcRepository extends ReactiveCrudRepository<StandingEntity, Long> {

    /**
     * Busca posiciones por liga y temporada, ordenadas por rank
     * @param leagueId ID de la liga
     * @param leagueSeason Año de la temporada
     * @return Flux con las posiciones ordenadas
     */
    Flux<StandingEntity> findByLeagueIdAndLeagueSeasonOrderByRankAsc(Long leagueId, Integer leagueSeason);

    /**
     * Busca la posición de un equipo en una liga y temporada
     * @param leagueId ID de la liga
     * @param leagueSeason Año de la temporada
     * @param teamId ID del equipo
     * @return Mono con la posición del equipo
     */
    Mono<StandingEntity> findByLeagueIdAndLeagueSeasonAndTeamId(Long leagueId, Integer leagueSeason, Long teamId);

    /**
     * Busca todas las posiciones de un equipo
     * @param teamId ID del equipo
     * @return Flux con las posiciones del equipo
     */
    Flux<StandingEntity> findByTeamId(Long teamId);

    /**
     * Elimina todas las posiciones de una liga y temporada
     * @param leagueId ID de la liga
     * @param leagueSeason Año de la temporada
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByLeagueIdAndLeagueSeason(Long leagueId, Integer leagueSeason);

    /**
     * Verifica si existe una posición para un equipo en liga y temporada
     * @param leagueId ID de la liga
     * @param leagueSeason Año de la temporada
     * @param teamId ID del equipo
     * @return Mono con true si existe
     */
    Mono<Boolean> existsByLeagueIdAndLeagueSeasonAndTeamId(Long leagueId, Integer leagueSeason, Long teamId);
}