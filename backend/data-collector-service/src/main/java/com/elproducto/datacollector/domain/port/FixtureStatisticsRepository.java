package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.FixtureStatistics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para el repositorio de estadísticas de partidos
 * Define el contrato para persistir y consultar estadísticas en la capa de dominio
 */
public interface FixtureStatisticsRepository {

    /**
     * Guarda una lista de estadísticas de partido de forma reactiva
     * @param statistics Lista de estadísticas a guardar
     * @return Mono con la lista de estadísticas guardadas
     */
    Mono<List<FixtureStatistics>> saveAll(List<FixtureStatistics> statistics);

    /**
     * Obtiene todas las estadísticas almacenadas
     * @return Flux con todas las estadísticas
     */
    Flux<FixtureStatistics> findAll();

    /**
     * Busca estadísticas por ID de partido
     * @param fixtureId ID del partido en API-Football
     * @return Flux con las estadísticas del partido (una por equipo)
     */
    Flux<FixtureStatistics> findByFixtureId(Long fixtureId);

    /**
     * Busca estadísticas de un equipo en un partido específico
     * @param fixtureId ID del partido en API-Football
     * @param teamId ID del equipo en API-Football
     * @return Mono con las estadísticas del equipo en ese partido
     */
    Mono<FixtureStatistics> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca todas las estadísticas de un equipo
     * @param teamId ID del equipo en API-Football
     * @return Flux con las estadísticas del equipo
     */
    Flux<FixtureStatistics> findByTeamId(Long teamId);

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