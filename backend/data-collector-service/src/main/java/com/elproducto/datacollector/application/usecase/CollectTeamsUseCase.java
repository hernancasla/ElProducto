package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.Team;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar equipos desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar equipos de un país específico
 */
@Service
public class CollectTeamsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectTeamsUseCase.class);

    private final FootballApiClient footballApiClient;
    private final TeamRepository teamRepository;

    public CollectTeamsUseCase(FootballApiClient footballApiClient,
                                TeamRepository teamRepository) {
        this.footballApiClient = footballApiClient;
        this.teamRepository = teamRepository;
    }

    /**
     * Ejecuta el proceso de recolección de equipos de forma reactiva
     * 1. Obtiene equipos de un país desde la API externa
     * 2. Los registra en el repositorio
     * 3. Retorna los equipos procesados
     *
     * @param country Nombre del país (ej: Argentina, Brazil, Spain)
     * @return Mono con la lista de equipos procesados
     */
    public Mono<List<Team>> execute(String country) {
        logger.info("🚀 Iniciando recolección de equipos para el país: {}", country);

        return footballApiClient.fetchTeamsByCountry(country)
                .doOnNext(teams -> {
                    logger.info("✅ Equipos obtenidos desde API: {}", teams.size());
                    // Log detallado de cada equipo
                    teams.forEach(team -> logger.debug("📋 Equipo recibido: {}", team));
                })
                .flatMap(teams -> {
                    // Guardar en repositorio de forma reactiva
                    return teamRepository.saveAll(teams)
                            .doOnSuccess(savedTeams ->
                                logger.info("💾 Equipos guardados en repositorio: {}", savedTeams.size())
                            );
                })
                .doOnSuccess(teams ->
                    logger.info("✅ Proceso de recolección completado exitosamente. Total de equipos: {}", teams.size())
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de equipos para país: {}", country, error)
                );
    }
}