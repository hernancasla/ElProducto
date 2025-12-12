package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.port.CountryRepository;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso: Recolectar países desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar países
 */
@Service
public class CollectCountriesUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectCountriesUseCase.class);

    private final FootballApiClient footballApiClient;
    private final CountryRepository countryRepository;

    public CollectCountriesUseCase(FootballApiClient footballApiClient,
                                    CountryRepository countryRepository) {
        this.footballApiClient = footballApiClient;
        this.countryRepository = countryRepository;
    }

    /**
     * Ejecuta el proceso de recolección de países
     * 1. Obtiene países desde la API externa
     * 2. Los registra en el repositorio
     * 3. Retorna los países procesados
     */
    public List<Country> execute() {
        logger.info("Iniciando recolección de países desde API-Football");

        // 1. Obtener países desde la API
        List<Country> countries = footballApiClient.fetchCountries();
        logger.info("Países obtenidos desde API: {}", countries.size());

        // Log detallado de cada país
        countries.forEach(country -> {
            logger.debug("País recibido: {}", country);
        });

        // 2. Guardar en repositorio
        List<Country> savedCountries = countryRepository.saveAll(countries);
        logger.info("Países guardados en repositorio: {}", savedCountries.size());

        logger.info("Proceso de recolección completado exitosamente");
        return savedCountries;
    }
}