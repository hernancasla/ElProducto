package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Country;
import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para repositorio de países
 * Define el contrato para persistencia de datos
 */
public interface CountryRepository {

    /**
     * Guarda una lista de países
     * @param countries Lista de países a guardar
     * @return Lista de países guardados
     */
    List<Country> saveAll(List<Country> countries);

    /**
     * Obtiene todos los países almacenados
     * @return Lista de todos los países
     */
    List<Country> findAll();
}