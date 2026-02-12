package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.MatchLineup;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.MatchLineupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Caso de uso: Recolectar alineaciones de un partido desde la API externa
 * Orquesta la logica de negocio para obtener y almacenar alineaciones de un partido especifico
 */
@Service
public class CollectMatchLineupsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectMatchLineupsUseCase.class);

    private final FootballApiClient footballApiClient;
    private final MatchLineupRepository lineupRepository;

    public CollectMatchLineupsUseCase(FootballApiClient footballApiClient,
                                       MatchLineupRepository lineupRepository) {
        this.footballApiClient = footballApiClient;
        this.lineupRepository = lineupRepository;
    }

    /**
     * Ejecuta el proceso de recoleccion de alineaciones de forma reactiva
     * 1. Obtiene alineaciones de un partido desde la API externa
     * 2. Las registra en el repositorio
     * 3. Retorna las alineaciones procesadas
     *
     * @param fixtureId ID del partido en API-Football
     * @return Mono con la lista de alineaciones procesadas
     */
    public Mono<List<MatchLineup>> execute(Long fixtureId) {
        logger.info("Iniciando recoleccion de alineaciones para el partido: {}", fixtureId);

        if (fixtureId == null || fixtureId <= 0) {
            return Mono.error(new IllegalArgumentException("Fixture ID must be positive"));
        }

        return footballApiClient.fetchLineupsByFixture(fixtureId)
                .doOnNext(lineups -> {
                    logger.info("Alineaciones obtenidas desde API: {} jugadores", lineups.size());
                    logLineupSummary(lineups);
                })
                .flatMap(lineups -> {
                    if (lineups.isEmpty()) {
                        logger.warn("No se encontraron alineaciones para el partido {}", fixtureId);
                        return Mono.just(lineups);
                    }
                    // Guardar en repositorio de forma reactiva
                    return lineupRepository.saveAll(lineups)
                            .doOnSuccess(savedLineups ->
                                logger.info("Alineaciones guardadas en repositorio: {} jugadores", savedLineups.size())
                            );
                })
                .doOnSuccess(lineups ->
                    logger.info("Proceso de recoleccion completado exitosamente. Total de jugadores: {}", lineups.size())
                )
                .doOnError(error ->
                    logger.error("Error en el proceso de recoleccion de alineaciones para partido: {}", fixtureId, error)
                );
    }

    /**
     * Log resumen de alineaciones por equipo
     */
    private void logLineupSummary(List<MatchLineup> lineups) {
        // Agrupar por equipo
        Map<String, List<MatchLineup>> byTeam = lineups.stream()
                .collect(Collectors.groupingBy(MatchLineup::teamName));

        byTeam.forEach((teamName, teamLineups) -> {
            long starters = teamLineups.stream().filter(MatchLineup::isStarter).count();
            long substitutes = teamLineups.stream().filter(l -> !l.isStarter()).count();
            String formation = teamLineups.stream()
                    .findFirst()
                    .map(MatchLineup::formation)
                    .orElse("N/A");

            logger.info("{}: Formacion {}, {} titulares, {} suplentes",
                    teamName, formation, starters, substitutes);

            // Log del entrenador
            teamLineups.stream()
                    .findFirst()
                    .ifPresent(lineup -> {
                        if (lineup.coachName() != null) {
                            logger.info("  DT: {}", lineup.coachName());
                        }
                    });

            // Log de titulares
            logger.info("  Titulares:");
            teamLineups.stream()
                    .filter(MatchLineup::isStarter)
                    .forEach(lineup -> logger.info("    #{} {} ({})",
                            lineup.playerNumber() != null ? lineup.playerNumber() : "?",
                            lineup.playerName(),
                            lineup.getPositionFull()));
        });
    }
}