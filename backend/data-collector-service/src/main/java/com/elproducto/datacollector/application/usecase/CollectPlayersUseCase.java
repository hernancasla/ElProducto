package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.Player;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar jugadores desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar jugadores de un equipo específico
 */
@Service
public class CollectPlayersUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectPlayersUseCase.class);

    private final FootballApiClient footballApiClient;
    private final PlayerRepository playerRepository;

    public CollectPlayersUseCase(FootballApiClient footballApiClient,
                                  PlayerRepository playerRepository) {
        this.footballApiClient = footballApiClient;
        this.playerRepository = playerRepository;
    }

    /**
     * Ejecuta el proceso de recolección de jugadores de forma reactiva
     * 1. Obtiene jugadores de un equipo desde la API externa para una temporada específica
     * 2. Los registra en el repositorio
     * 3. Retorna los jugadores procesados
     *
     * @param teamId ID del equipo en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Mono con la lista de jugadores procesados
     */
    public Mono<List<Player>> execute(Long teamId, Integer season) {
        logger.info("🚀 Iniciando recolección de jugadores para el equipo: {} temporada: {}", teamId, season);

        if (teamId == null || teamId <= 0) {
            return Mono.error(new IllegalArgumentException("Team ID must be positive"));
        }
        if (season == null || season < 1900 || season > 2100) {
            return Mono.error(new IllegalArgumentException("Season must be a valid year between 1900 and 2100"));
        }

        return footballApiClient.fetchPlayersByTeam(teamId, season)
                .doOnNext(players -> {
                    logger.info("✅ Jugadores obtenidos desde API: {}", players.size());
                    // Log detallado de cada jugador
                    players.forEach(player -> logger.debug("📋 Jugador recibido: {}", player));
                })
                .flatMap(players -> {
                    if (players.isEmpty()) {
                        logger.warn("⚠️ No se encontraron jugadores para el equipo {} temporada {}", teamId, season);
                        return Mono.just(players);
                    }
                    // Guardar en repositorio de forma reactiva
                    return playerRepository.saveAll(players)
                            .doOnSuccess(savedPlayers ->
                                logger.info("💾 Jugadores guardados en repositorio: {}", savedPlayers.size())
                            );
                })
                .doOnSuccess(players ->
                    logger.info("✅ Proceso de recolección completado exitosamente. Total de jugadores: {}", players.size())
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de jugadores para equipo: {} temporada: {}", teamId, season, error)
                );
    }
}