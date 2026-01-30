package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.Standing;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.StandingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar tabla de posiciones desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar posiciones de una liga específica
 */
@Service
public class CollectStandingsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectStandingsUseCase.class);

    private final FootballApiClient footballApiClient;
    private final StandingRepository standingRepository;

    public CollectStandingsUseCase(FootballApiClient footballApiClient,
                                    StandingRepository standingRepository) {
        this.footballApiClient = footballApiClient;
        this.standingRepository = standingRepository;
    }

    /**
     * Ejecuta el proceso de recolección de posiciones de forma reactiva
     * 1. Obtiene posiciones de una liga desde la API externa para una temporada específica
     * 2. Las registra en el repositorio
     * 3. Retorna las posiciones procesadas
     *
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Mono con la lista de posiciones procesadas
     */
    public Mono<List<Standing>> execute(Long leagueId, Integer season) {
        logger.info("🚀 Iniciando recolección de posiciones para la liga: {} temporada: {}", leagueId, season);

        if (leagueId == null || leagueId <= 0) {
            return Mono.error(new IllegalArgumentException("League ID must be positive"));
        }
        if (season == null || season < 1900 || season > 2100) {
            return Mono.error(new IllegalArgumentException("Season must be a valid year between 1900 and 2100"));
        }

        return footballApiClient.fetchStandingsByLeague(leagueId, season)
                .doOnNext(standings -> {
                    logger.info("✅ Posiciones obtenidas desde API: {}", standings.size());
                    // Log del líder si existe
                    standings.stream()
                            .filter(s -> s.rank() == 1)
                            .findFirst()
                            .ifPresent(leader -> logger.info("🏆 Líder: {} con {} puntos",
                                    leader.teamName(), leader.points()));
                })
                .flatMap(standings -> {
                    if (standings.isEmpty()) {
                        logger.warn("⚠️ No se encontraron posiciones para la liga {} temporada {}", leagueId, season);
                        return Mono.just(standings);
                    }
                    // Guardar en repositorio de forma reactiva
                    return standingRepository.saveAll(standings)
                            .doOnSuccess(savedStandings ->
                                logger.info("💾 Posiciones guardadas en repositorio: {}", savedStandings.size())
                            );
                })
                .doOnSuccess(standings ->
                    logger.info("✅ Proceso de recolección completado exitosamente. Total de posiciones: {}", standings.size())
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de posiciones para liga: {} temporada: {}", leagueId, season, error)
                );
    }
}