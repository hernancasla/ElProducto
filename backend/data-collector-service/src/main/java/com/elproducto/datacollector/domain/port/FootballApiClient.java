package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Country;
import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para cliente de API externa
 * Define el contrato para obtener datos de la API de fútbol
 */
public interface FootballApiClient {

    /**
     * Obtiene la lista de países disponibles en la API
     * @return Lista de países
     */
    List<Country> fetchCountries();
}