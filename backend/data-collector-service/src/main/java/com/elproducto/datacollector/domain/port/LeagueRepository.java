package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.League;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de ligas
 * Define el contrato para persistir y recuperar ligas
 */
public interface LeagueRepository {

    /**
     * Guarda una lista de ligas de forma reactiva
     * @param leagues Lista de ligas a guardar
     * @return Mono con la lista de ligas guardadas
     */
    Mono<List<League>> saveAll(List<League> leagues);

    /**
     * Busca todas las ligas de un país
     * @param countryCode Código ISO del país
     * @return Flux con las ligas del país
     */
    Flux<League> findByCountryCode(String countryCode);

    /**
     * Busca todas las ligas almacenadas
     * @return Flux con todas las ligas
     */
    Flux<League> findAll();

    /**
     * Busca una liga por su ID de API-Football
     * @param apiFootballId ID de la liga en API-Football
     * @return Mono con la liga si existe
     */
    Mono<League> findByApiFootballId(Long apiFootballId);
}