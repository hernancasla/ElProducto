package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.FixtureStatistics;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.FixtureStatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar estadísticas de un partido desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar estadísticas de un partido específico
 */
@Service
public class CollectFixtureStatisticsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectFixtureStatisticsUseCase.class);

    private final FootballApiClient footballApiClient;
    private final FixtureStatisticsRepository statisticsRepository;

    public CollectFixtureStatisticsUseCase(FootballApiClient footballApiClient,
                                            FixtureStatisticsRepository statisticsRepository) {
        this.footballApiClient = footballApiClient;
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Ejecuta el proceso de recolección de estadísticas de forma reactiva
     * 1. Obtiene estadísticas de un partido desde la API externa
     * 2. Las registra en el repositorio
     * 3. Retorna las estadísticas procesadas
     *
     * @param fixtureId ID del partido en API-Football
     * @return Mono con la lista de estadísticas procesadas (una por equipo)
     */
    public Mono<List<FixtureStatistics>> execute(Long fixtureId) {
        logger.info("🚀 Iniciando recolección de estadísticas para el partido: {}", fixtureId);

        if (fixtureId == null || fixtureId <= 0) {
            return Mono.error(new IllegalArgumentException("Fixture ID must be positive"));
        }

        return footballApiClient.fetchStatisticsByFixture(fixtureId)
                .doOnNext(statistics -> {
                    logger.info("✅ Estadísticas obtenidas desde API: {} equipos", statistics.size());
                    // Log resumen de estadísticas
                    statistics.forEach(stat ->
                        logger.info("📊 {} - Posesión: {}%, Tiros: {}, Corners: {}",
                            stat.teamName(),
                            stat.ballPossession() != null ? stat.ballPossession() : "N/A",
                            stat.totalShots() != null ? stat.totalShots() : "N/A",
                            stat.cornerKicks() != null ? stat.cornerKicks() : "N/A")
                    );
                })
                .flatMap(statistics -> {
                    if (statistics.isEmpty()) {
                        logger.warn("⚠️ No se encontraron estadísticas para el partido {}", fixtureId);
                        return Mono.just(statistics);
                    }
                    // Guardar en repositorio de forma reactiva
                    return statisticsRepository.saveAll(statistics)
                            .doOnSuccess(savedStats ->
                                logger.info("💾 Estadísticas guardadas en repositorio: {} equipos", savedStats.size())
                            );
                })
                .doOnSuccess(statistics ->
                    logger.info("✅ Proceso de recolección completado exitosamente. Total de estadísticas: {}", statistics.size())
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de estadísticas para partido: {}", fixtureId, error)
                );
    }
}