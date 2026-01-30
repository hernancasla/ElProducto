package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Standing;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de posiciones
 * Define el contrato para persistencia de datos de forma reactiva
 */
public interface StandingRepository {

    /**
     * Guarda una lista de posiciones de forma reactiva
     * @param standings Lista de posiciones a guardar
     * @return Mono con la lista de posiciones guardadas
     */
    Mono<List<Standing>> saveAll(List<Standing> standings);

    /**
     * Obtiene todas las posiciones almacenadas de forma reactiva
     * @return Flux con todas las posiciones
     */
    Flux<Standing> findAll();

    /**
     * Obtiene las posiciones de una liga específica para una temporada
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada
     * @return Flux con las posiciones ordenadas por rank
     */
    Flux<Standing> findByLeagueIdAndSeason(Long leagueId, Integer season);

    /**
     * Obtiene la posición de un equipo específico en una liga y temporada
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada
     * @param teamId ID del equipo en API-Football
     * @return Mono con la posición del equipo
     */
    Mono<Standing> findByLeagueIdAndSeasonAndTeamId(Long leagueId, Integer season, Long teamId);

    /**
     * Obtiene todas las posiciones de un equipo (en diferentes ligas/temporadas)
     * @param teamId ID del equipo en API-Football
     * @return Flux con las posiciones del equipo
     */
    Flux<Standing> findByTeamId(Long teamId);

    /**
     * Elimina todas las posiciones de una liga y temporada
     * Útil para actualizar completamente la tabla
     * @param leagueId ID de la liga
     * @param season Año de la temporada
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByLeagueIdAndSeason(Long leagueId, Integer season);
}