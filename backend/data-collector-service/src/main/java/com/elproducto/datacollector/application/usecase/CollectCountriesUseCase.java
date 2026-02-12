package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.port.CountryRepository;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
     * Ejecuta el proceso de recolección de países de forma reactiva
     * 1. Obtiene países desde la API externa
     * 2. Los registra en el repositorio
     * 3. Retorna los países procesados
     */
    public Mono<List<Country>> execute() {
        logger.info("Iniciando recolección de países desde API-Football");

        return footballApiClient.fetchCountries()
                .doOnNext(countries -> {
                    logger.info("Países obtenidos desde API: {}", countries.size());
                    // Log detallado de cada país
                    countries.forEach(country -> logger.debug("País recibido: {}", country));
                })
                .flatMap(countries -> {
                    // 2. Guardar en repositorio de forma reactiva
                    return countryRepository.saveAll(countries)
                            .doOnSuccess(savedCountries ->
                                logger.info("Países guardados en repositorio: {}", savedCountries.size())
                            );
                })
                .doOnSuccess(countries -> logger.info("Proceso de recolección completado exitosamente"))
                .doOnError(error -> logger.error("Error en el proceso de recolección", error));
    }
}