package com.elproducto.datacollector.application.usecase;

import com.elproducto.datacollector.domain.model.FixtureEvent;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.domain.port.FixtureEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Caso de uso: Recolectar eventos de un partido desde la API externa
 * Orquesta la lógica de negocio para obtener y almacenar eventos de un partido específico
 */
@Service
public class CollectFixtureEventsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CollectFixtureEventsUseCase.class);

    private final FootballApiClient footballApiClient;
    private final FixtureEventRepository eventRepository;

    public CollectFixtureEventsUseCase(FootballApiClient footballApiClient,
                                        FixtureEventRepository eventRepository) {
        this.footballApiClient = footballApiClient;
        this.eventRepository = eventRepository;
    }

    /**
     * Ejecuta el proceso de recolección de eventos de forma reactiva
     * 1. Obtiene eventos de un partido desde la API externa
     * 2. Los registra en el repositorio
     * 3. Retorna los eventos procesados
     *
     * @param fixtureId ID del partido en API-Football
     * @return Mono con la lista de eventos procesados
     */
    public Mono<List<FixtureEvent>> execute(Long fixtureId) {
        logger.info("🚀 Iniciando recolección de eventos para el partido: {}", fixtureId);

        if (fixtureId == null || fixtureId <= 0) {
            return Mono.error(new IllegalArgumentException("Fixture ID must be positive"));
        }

        return footballApiClient.fetchEventsByFixture(fixtureId)
                .doOnNext(events -> {
                    logger.info("✅ Eventos obtenidos desde API: {}", events.size());
                    // Log resumen de eventos por tipo
                    long goals = events.stream().filter(FixtureEvent::isGoal).count();
                    long yellowCards = events.stream().filter(FixtureEvent::isYellowCard).count();
                    long redCards = events.stream().filter(FixtureEvent::isRedCard).count();
                    long subs = events.stream().filter(FixtureEvent::isSubstitution).count();

                    logger.info("📊 Resumen: {} goles, {} amarillas, {} rojas, {} cambios",
                            goals, yellowCards, redCards, subs);

                    // Log de goles si existen
                    events.stream()
                            .filter(FixtureEvent::isGoal)
                            .forEach(goal -> logger.info("⚽ {}' {} ({}) - {}",
                                    goal.getFullTime(), goal.playerName(), goal.teamName(), goal.detail()));
                })
                .flatMap(events -> {
                    if (events.isEmpty()) {
                        logger.warn("⚠️ No se encontraron eventos para el partido {}", fixtureId);
                        return Mono.just(events);
                    }
                    // Guardar en repositorio de forma reactiva
                    return eventRepository.saveAll(events)
                            .doOnSuccess(savedEvents ->
                                logger.info("💾 Eventos guardados en repositorio: {}", savedEvents.size())
                            );
                })
                .doOnSuccess(events ->
                    logger.info("✅ Proceso de recolección completado exitosamente. Total de eventos: {}", events.size())
                )
                .doOnError(error ->
                    logger.error("❌ Error en el proceso de recolección de eventos para partido: {}", fixtureId, error)
                );
    }
}