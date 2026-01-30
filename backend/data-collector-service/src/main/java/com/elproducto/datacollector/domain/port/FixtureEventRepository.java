package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.FixtureEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para el repositorio de eventos de partidos
 * Define el contrato para persistir y consultar eventos en la capa de dominio
 */
public interface FixtureEventRepository {

    /**
     * Guarda una lista de eventos de partido de forma reactiva
     * @param events Lista de eventos a guardar
     * @return Mono con la lista de eventos guardados
     */
    Mono<List<FixtureEvent>> saveAll(List<FixtureEvent> events);

    /**
     * Obtiene todos los eventos almacenados
     * @return Flux con todos los eventos
     */
    Flux<FixtureEvent> findAll();

    /**
     * Busca eventos por ID de partido ordenados cronológicamente
     * @param fixtureId ID del partido en API-Football
     * @return Flux con los eventos del partido
     */
    Flux<FixtureEvent> findByFixtureIdOrderByTimeElapsedAsc(Long fixtureId);

    /**
     * Busca eventos de un equipo en un partido específico
     * @param fixtureId ID del partido en API-Football
     * @param teamId ID del equipo en API-Football
     * @return Flux con los eventos del equipo en ese partido
     */
    Flux<FixtureEvent> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);

    /**
     * Busca eventos por tipo (Goal, Card, subst)
     * @param fixtureId ID del partido en API-Football
     * @param type Tipo de evento
     * @return Flux con los eventos del tipo especificado
     */
    Flux<FixtureEvent> findByFixtureIdAndType(Long fixtureId, String type);

    /**
     * Busca goles de un partido
     * @param fixtureId ID del partido
     * @return Flux con los goles del partido
     */
    Flux<FixtureEvent> findGoalsByFixtureId(Long fixtureId);

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