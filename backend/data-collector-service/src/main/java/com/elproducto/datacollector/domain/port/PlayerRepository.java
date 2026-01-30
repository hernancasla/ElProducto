package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de jugadores
 * Define el contrato para persistencia de datos de forma reactiva
 */
public interface PlayerRepository {

    /**
     * Guarda una lista de jugadores de forma reactiva
     * @param players Lista de jugadores a guardar
     * @return Mono con la lista de jugadores guardados
     */
    Mono<List<Player>> saveAll(List<Player> players);

    /**
     * Obtiene todos los jugadores almacenados de forma reactiva
     * @return Flux con todos los jugadores
     */
    Flux<Player> findAll();

    /**
     * Obtiene todos los jugadores de un equipo específico
     * @param teamId ID del equipo en API-Football
     * @return Flux con los jugadores del equipo
     */
    Flux<Player> findByTeamId(Long teamId);

    /**
     * Obtiene todos los jugadores de una nacionalidad específica
     * @param nationality Nacionalidad del jugador
     * @return Flux con los jugadores de esa nacionalidad
     */
    Flux<Player> findByNationality(String nationality);

    /**
     * Busca un jugador por su ID de API-Football
     * @param apiFootballId ID del jugador en API-Football
     * @return Mono con el jugador encontrado o vacío
     */
    Mono<Player> findByApiFootballId(Long apiFootballId);
}