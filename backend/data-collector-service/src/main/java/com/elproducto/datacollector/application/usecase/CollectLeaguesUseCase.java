package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.League;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.LeagueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar ligas desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar ligas de un país
 */
@Service
public class CollectLeaguesUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectLeaguesUseCase.class);

    private final FootballApiClient footballApiClient;
    private final LeagueRepository leagueRepository;

    public CollectLeaguesUseCase(FootballApiClient footballApiClient,
                                  LeagueRepository leagueRepository) {
        this.footballApiClient = footballApiClient;
        this.leagueRepository = leagueRepository;
    }

    /**
     * Ejecuta el proceso de recolección de ligas de forma reactiva
     * @param countryCode Código ISO del país (ej: AR, BR, ES)
     * @return Mono con la lista de ligas procesadas
     *
     * Flujo:
     * 1. Obtiene ligas desde la API externa para el país especificado
     * 2. Las registra en el repositorio (con sus temporadas y coberturas)
     * 3. Retorna las ligas procesadas
     */
    public Mono<List<League>> execute(String countryCode) {
        logger.info("🚀 Iniciando recolección de ligas para país: {}", countryCode);

        if (countryCode == null || countryCode.isBlank()) {
            logger.error("❌ Código de país inválido");
            return Mono.error(new IllegalArgumentException("Country code cannot be null or empty"));
        }

        return footballApiClient.fetchLeaguesByCountry(countryCode)
                .doOnNext(leagues -> {
                    logger.info("✅ Ligas obtenidas desde API para {}: {}", countryCode, leagues.size());
                    // Log detallado de cada liga
                    leagues.forEach(league -> logger.debug("Liga recibida: {}", league));
                })
                .flatMap(leagues -> {
                    if (leagues.isEmpty()) {
                        logger.warn("⚠️ No se encontraron ligas para el país: {}", countryCode);
                        return Mono.just(leagues);
                    }

                    // Guardar en repositorio de forma reactiva
                    return leagueRepository.saveAll(leagues)
                            .doOnSuccess(savedLeagues ->
                                logger.info("💾 Ligas guardadas en repositorio para {}: {}",
                                    countryCode, savedLeagues.size())
                            );
                })
                .doOnSuccess(leagues ->
                    logger.info("✅ Proceso de recolección de ligas completado exitosamente para {}", countryCode)
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de ligas para {}", countryCode, error)
                );
    }
}