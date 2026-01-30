package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Team;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de equipos
 * Define el contrato para persistencia de datos de forma reactiva
 */
public interface TeamRepository {

    /**
     * Guarda una lista de equipos de forma reactiva
     * @param teams Lista de equipos a guardar
     * @return Mono con la lista de equipos guardados
     */
    Mono<List<Team>> saveAll(List<Team> teams);

    /**
     * Obtiene todos los equipos almacenados de forma reactiva
     * @return Flux con todos los equipos
     */
    Flux<Team> findAll();

    /**
     * Obtiene todos los equipos de un país específico
     * @param country Nombre del país
     * @return Flux con los equipos del país
     */
    Flux<Team> findByCountry(String country);
}