package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.Fixture;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.FixtureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar partidos desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar partidos de una liga específica
 */
@Service
public class CollectFixturesUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectFixturesUseCase.class);

    private final FootballApiClient footballApiClient;
    private final FixtureRepository fixtureRepository;

    public CollectFixturesUseCase(FootballApiClient footballApiClient,
                                   FixtureRepository fixtureRepository) {
        this.footballApiClient = footballApiClient;
        this.fixtureRepository = fixtureRepository;
    }

    /**
     * Ejecuta el proceso de recolección de partidos de forma reactiva
     * 1. Obtiene partidos de una liga desde la API externa para una temporada específica
     * 2. Los registra en el repositorio
     * 3. Retorna los partidos procesados
     *
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Mono con la lista de partidos procesados
     */
    public Mono<List<Fixture>> execute(Long leagueId, Integer season) {
        logger.info("🚀 Iniciando recolección de partidos para la liga: {} temporada: {}", leagueId, season);

        if (leagueId == null || leagueId <= 0) {
            return Mono.error(new IllegalArgumentException("League ID must be positive"));
        }
        if (season == null || season < 1900 || season > 2100) {
            return Mono.error(new IllegalArgumentException("Season must be a valid year between 1900 and 2100"));
        }

        return footballApiClient.fetchFixturesByLeague(leagueId, season)
                .doOnNext(fixtures -> {
                    logger.info("✅ Partidos obtenidos desde API: {}", fixtures.size());
                    // Log resumen de partidos
                    long finished = fixtures.stream().filter(Fixture::isFinished).count();
                    long scheduled = fixtures.stream().filter(Fixture::isScheduled).count();
                    long live = fixtures.stream().filter(Fixture::isLive).count();
                    logger.info("📊 Resumen: {} finalizados, {} programados, {} en vivo", finished, scheduled, live);
                })
                .flatMap(fixtures -> {
                    if (fixtures.isEmpty()) {
                        logger.warn("⚠️ No se encontraron partidos para la liga {} temporada {}", leagueId, season);
                        return Mono.just(fixtures);
                    }
                    // Guardar en repositorio de forma reactiva
                    return fixtureRepository.saveAll(fixtures)
                            .doOnSuccess(savedFixtures ->
                                logger.info("💾 Partidos guardados en repositorio: {}", savedFixtures.size())
                            );
                })
                .doOnSuccess(fixtures ->
                    logger.info("✅ Proceso de recolección completado exitosamente. Total de partidos: {}", fixtures.size())
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de partidos para liga: {} temporada: {}", leagueId, season, error)
                );
    }
}