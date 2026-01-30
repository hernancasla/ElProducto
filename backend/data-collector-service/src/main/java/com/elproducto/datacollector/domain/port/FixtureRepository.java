package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Fixture;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de partidos
 * Define el contrato para persistencia de datos de forma reactiva
 */
public interface FixtureRepository {

    /**
     * Guarda una lista de partidos de forma reactiva
     * @param fixtures Lista de partidos a guardar
     * @return Mono con la lista de partidos guardados
     */
    Mono<List<Fixture>> saveAll(List<Fixture> fixtures);

    /**
     * Obtiene todos los partidos almacenados de forma reactiva
     * @return Flux con todos los partidos
     */
    Flux<Fixture> findAll();

    /**
     * Obtiene todos los partidos de una liga específica para una temporada
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada
     * @return Flux con los partidos de la liga
     */
    Flux<Fixture> findByLeagueIdAndSeason(Long leagueId, Integer season);

    /**
     * Obtiene todos los partidos de un equipo específico
     * @param teamId ID del equipo en API-Football
     * @return Flux con los partidos del equipo (como local o visitante)
     */
    Flux<Fixture> findByTeamId(Long teamId);

    /**
     * Obtiene partidos en un rango de fechas
     * @param from Fecha desde
     * @param to Fecha hasta
     * @return Flux con los partidos en el rango
     */
    Flux<Fixture> findByDateBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Busca un partido por su ID de API-Football
     * @param apiFootballId ID del partido en API-Football
     * @return Mono con el partido encontrado o vacío
     */
    Mono<Fixture> findByApiFootballId(Long apiFootballId);

    /**
     * Obtiene partidos por estado
     * @param statusShort Código corto del estado (NS, 1H, HT, 2H, FT, etc.)
     * @return Flux con los partidos en ese estado
     */
    Flux<Fixture> findByStatus(String statusShort);
}