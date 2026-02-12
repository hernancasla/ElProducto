package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Country;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de países
 * Define el contrato para persistencia de datos de forma reactiva
 */
public interface CountryRepository {

    /**
     * Guarda una lista de países de forma reactiva
     * @param countries Lista de países a guardar
     * @return Mono con la lista de países guardados
     */
    Mono<List<Country>> saveAll(List<Country> countries);

    /**
     * Obtiene todos los países almacenados de forma reactiva
     * @return Flux con todos los países
     */
    Flux<Country> findAll();
}