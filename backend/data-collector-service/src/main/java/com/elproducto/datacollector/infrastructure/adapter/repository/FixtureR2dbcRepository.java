package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.FixtureEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla fixtures
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface FixtureR2dbcRepository extends ReactiveCrudRepository<FixtureEntity, Long> {

    /**
     * Busca un fixture por su ID de API-Football
     * @param apiFootballId ID del partido en API-Football
     * @return Mono con la entidad del fixture si existe
     */
    Mono<FixtureEntity> findByApiFootballId(Long apiFootballId);

    /**
     * Busca fixtures por liga y temporada
     * @param leagueId ID de la liga
     * @param leagueSeason Año de la temporada
     * @return Flux con los fixtures
     */
    Flux<FixtureEntity> findByLeagueIdAndLeagueSeason(Long leagueId, Integer leagueSeason);

    /**
     * Busca fixtures donde participa un equipo (como local o visitante)
     * @param homeTeamId ID del equipo local
     * @param awayTeamId ID del equipo visitante
     * @return Flux con los fixtures del equipo
     */
    @Query("SELECT * FROM fixtures WHERE home_team_id = :homeTeamId OR away_team_id = :awayTeamId ORDER BY fixture_date DESC")
    Flux<FixtureEntity> findByTeamId(Long homeTeamId, Long awayTeamId);

    /**
     * Busca fixtures en un rango de fechas
     * @param from Fecha desde
     * @param to Fecha hasta
     * @return Flux con los fixtures en el rango
     */
    Flux<FixtureEntity> findByFixtureDateBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Busca fixtures por estado
     * @param statusShort Código corto del estado
     * @return Flux con los fixtures en ese estado
     */
    Flux<FixtureEntity> findByStatusShort(String statusShort);

    /**
     * Busca fixtures de un equipo local
     * @param homeTeamId ID del equipo local
     * @return Flux con los fixtures
     */
    Flux<FixtureEntity> findByHomeTeamId(Long homeTeamId);

    /**
     * Busca fixtures de un equipo visitante
     * @param awayTeamId ID del equipo visitante
     * @return Flux con los fixtures
     */
    Flux<FixtureEntity> findByAwayTeamId(Long awayTeamId);

    /**
     * Elimina un fixture por su ID de API-Football
     * @param apiFootballId ID del fixture en API-Football
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByApiFootballId(Long apiFootballId);

    /**
     * Verifica si existe un fixture con el ID de API-Football dado
     * @param apiFootballId ID del fixture en API-Football
     * @return Mono con true si existe, false si no
     */
    Mono<Boolean> existsByApiFootballId(Long apiFootballId);

    /**
     * Busca fixtures por lista de estados
     * @param statuses Lista de códigos de estado
     * @return Flux con los fixtures en esos estados
     */
    Flux<FixtureEntity> findByStatusShortIn(java.util.Collection<String> statuses);
}