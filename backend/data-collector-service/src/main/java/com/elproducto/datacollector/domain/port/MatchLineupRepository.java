package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.MatchLineup;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para el repositorio de alineaciones
 * Define el contrato para persistir y consultar alineaciones en la capa de dominio
 */
public interface MatchLineupRepository {

    /**
     * Guarda una lista de alineaciones de partido de forma reactiva
     * @param lineups Lista de jugadores en la alineación a guardar
     * @return Mono con la lista de alineaciones guardadas
     */
    Mono<List<MatchLineup>> saveAll(List<MatchLineup> lineups);

    /**
     * Obtiene todas las alineaciones almacenadas
     * @return Flux con todas las alineaciones
     */
    Flux<MatchLineup> findAll();

    /**
     * Busca alineaciones por ID de partido
     * @param fixtureId ID del partido en API-Football
     * @return Flux con las alineaciones del partido
     */
    Flux<MatchLineup> findByFixtureId(Long fixtureId);

    /**
     * Busca alineación de un equipo en un partido específico
     * @param fixtureId ID del partido en API-Football
     * @param teamId ID del equipo en API-Football
     * @return Flux con la alineación del equipo
     */
    Flux<MatchLineup> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca titulares de un equipo en un partido
     * @param fixtureId ID del partido
     * @param teamId ID del equipo
     * @return Flux con los jugadores titulares
     */
    Flux<MatchLineup> findStartersByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca suplentes de un equipo en un partido
     * @param fixtureId ID del partido
     * @param teamId ID del equipo
     * @return Flux con los jugadores suplentes
     */
    Flux<MatchLineup> findSubstitutesByFixtureIdAndTeamId(Long fixtureId, Long teamId);

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
     * Cuenta los jugadores en la alineación de un partido
     * @param fixtureId ID del partido
     * @return Mono con el número de jugadores
     */
    Mono<Long> countByFixtureId(Long fixtureId);
}