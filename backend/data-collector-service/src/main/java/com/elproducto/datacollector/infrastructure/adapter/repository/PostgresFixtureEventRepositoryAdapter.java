package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.FixtureEvent;
import com.elproducto.datacollector.domain.port.FixtureEventRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.FixtureEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Adaptador de repositorio para PostgreSQL usando R2DBC
 * Implementa el puerto FixtureEventRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresFixtureEventRepositoryAdapter implements FixtureEventRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresFixtureEventRepositoryAdapter.class);

    private final FixtureEventR2dbcRepository eventRepository;

    public PostgresFixtureEventRepositoryAdapter(FixtureEventR2dbcRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<List<FixtureEvent>> saveAll(List<FixtureEvent> events) {
        logger.info("💾 [POSTGRES] Guardando {} eventos en PostgreSQL", events.size());

        // Para eventos, primero eliminamos los existentes del partido y luego insertamos los nuevos
        // Esto garantiza que siempre tengamos la lista completa actualizada
        if (events.isEmpty()) {
            return Mono.just(List.of());
        }

        Long fixtureId = events.get(0).fixtureId();

        return eventRepository.deleteByFixtureId(fixtureId)
                .then(Flux.fromIterable(events)
                        .map(FixtureEventEntity::fromDomain)
                        .collectList()
                        .flatMap(entities -> eventRepository.saveAll(entities).collectList())
                )
                .map(savedEntities -> savedEntities.stream()
                        .map(FixtureEventEntity::toDomain)
                        .toList()
                )
                .doOnSuccess(savedEvents ->
                    logger.info("💾 [POSTGRES] {} eventos guardados exitosamente para partido {}", savedEvents.size(), fixtureId)
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando eventos", error)
                );
    }

    @Override
    public Flux<FixtureEvent> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todos los eventos");

        return eventRepository.findAll()
                .map(FixtureEventEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de eventos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando eventos", error));
    }

    @Override
    public Flux<FixtureEvent> findByFixtureIdOrderByTimeElapsedAsc(Long fixtureId) {
        logger.info("💾 [POSTGRES] Recuperando eventos del partido {} ordenados cronológicamente", fixtureId);

        return eventRepository.findByFixtureIdOrderByTimeElapsedAscTimeExtraAsc(fixtureId)
                .map(FixtureEventEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de eventos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando eventos por partido", error));
    }

    @Override
    public Flux<FixtureEvent> findByFixtureIdAndTeamId(Long fixtureId, Long teamId) {
        logger.info("💾 [POSTGRES] Recuperando eventos del equipo {} en partido {}", teamId, fixtureId);

        return eventRepository.findByFixtureIdAndTeamId(fixtureId, teamId)
                .map(FixtureEventEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de eventos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando eventos por equipo", error));
    }

    @Override
    public Flux<FixtureEvent> findByFixtureIdAndType(Long fixtureId, String type) {
        logger.info("💾 [POSTGRES] Recuperando eventos tipo {} del partido {}", type, fixtureId);

        return eventRepository.findByFixtureIdAndType(fixtureId, type)
                .map(FixtureEventEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de eventos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando eventos por tipo", error));
    }

    @Override
    public Flux<FixtureEvent> findGoalsByFixtureId(Long fixtureId) {
        logger.info("💾 [POSTGRES] Recuperando goles del partido {}", fixtureId);

        return findByFixtureIdAndType(fixtureId, "Goal");
    }

    @Override
    public Mono<Void> deleteByFixtureId(Long fixtureId) {
        logger.info("💾 [POSTGRES] Eliminando eventos del partido {}", fixtureId);

        return eventRepository.deleteByFixtureId(fixtureId)
                .doOnSuccess(v -> logger.info("💾 [POSTGRES] Eventos eliminados exitosamente"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error eliminando eventos", error));
    }

    @Override
    public Mono<Boolean> existsByFixtureId(Long fixtureId) {
        return eventRepository.existsByFixtureId(fixtureId);
    }

    @Override
    public Mono<Long> countByFixtureId(Long fixtureId) {
        return eventRepository.countByFixtureId(fixtureId);
    }
}