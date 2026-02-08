package com.elproducto.datacollector.infrastructure.scheduler;

import com.elproducto.datacollector.application.service.SyncService;
import com.elproducto.datacollector.domain.model.Fixture;
import com.elproducto.datacollector.domain.model.SyncTrigger;
import com.elproducto.datacollector.domain.port.FixtureRepository;
import com.elproducto.datacollector.infrastructure.config.SyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Scheduler para sincronizacion de partidos en vivo
 * Actualiza events y stats de partidos que estan en juego
 */
@Component
@ConditionalOnProperty(name = "sync.enabled", havingValue = "true")
public class LiveMatchSyncScheduler {

    private static final Logger logger = LoggerFactory.getLogger(LiveMatchSyncScheduler.class);

    private final SyncService syncService;
    private final FixtureRepository fixtureRepository;
    private final SyncConfiguration syncConfiguration;

    // Estados de partidos en vivo (API-Football)
    private static final Set<String> LIVE_STATUSES = Set.of(
        "1H",   // Primera mitad
        "HT",   // Entretiempo
        "2H",   // Segunda mitad
        "ET",   // Tiempo extra
        "BT",   // Break time (antes de tiempo extra)
        "P",    // Penales
        "SUSP", // Suspendido
        "INT",  // Interrumpido
        "LIVE"  // En vivo (generico)
    );

    // Estados de partido finalizado
    private static final Set<String> FINISHED_STATUSES = Set.of(
        "FT",   // Tiempo completo
        "AET",  // Despues de tiempo extra
        "PEN"   // Despues de penales
    );

    public LiveMatchSyncScheduler(
            SyncService syncService,
            FixtureRepository fixtureRepository,
            SyncConfiguration syncConfiguration) {
        this.syncService = syncService;
        this.fixtureRepository = fixtureRepository;
        this.syncConfiguration = syncConfiguration;
        logger.info("[LIVE-SYNC] Scheduler de partidos en vivo ACTIVADO");
    }

    /**
     * Sincroniza eventos de partidos en vivo cada 60 segundos
     */
    @Scheduled(fixedDelayString = "${sync.live-polling.interval:60000}")
    public void syncLiveMatches() {
        if (!syncConfiguration.isEnabled()) return;

        logger.debug("[LIVE-SYNC] Verificando partidos en vivo...");

        // Buscar partidos en vivo en la base de datos
        fixtureRepository.findByStatusShortIn(LIVE_STATUSES.stream().toList())
            .collectList()
            .flatMapMany(liveFixtures -> {
                if (liveFixtures.isEmpty()) {
                    logger.debug("[LIVE-SYNC] No hay partidos en vivo actualmente");
                    return Flux.empty();
                }

                logger.info("[LIVE-SYNC] Encontrados {} partidos en vivo", liveFixtures.size());

                return Flux.fromIterable(liveFixtures)
                    .flatMap(fixture -> {
                        logger.info("[LIVE-SYNC] Sincronizando fixture {} ({} vs {}) - Estado: {}",
                            fixture.apiFootballId(),
                            fixture.homeTeamName(),
                            fixture.awayTeamName(),
                            fixture.statusShort());

                        // Sincronizar eventos del partido en vivo
                        return syncService.syncFixtureEvents(fixture.apiFootballId(), SyncTrigger.EVENT)
                            .doOnSuccess(result -> {
                                if (!result.wasSkipped()) {
                                    logger.info("[LIVE-SYNC] Eventos sincronizados para fixture {}: {} eventos",
                                        fixture.apiFootballId(), result.processed());
                                }
                            })
                            .onErrorResume(e -> {
                                logger.warn("[LIVE-SYNC] Error sincronizando eventos del fixture {}: {}",
                                    fixture.apiFootballId(), e.getMessage());
                                return reactor.core.publisher.Mono.empty();
                            });
                    }, 2);  // Concurrencia limitada a 2 para no saturar la API
            })
            .subscribe();
    }

    /**
     * Sincroniza datos completos de partidos recien finalizados cada 5 minutos
     */
    @Scheduled(fixedDelayString = "${sync.finished-polling.interval:300000}")
    public void syncRecentlyFinishedMatches() {
        if (!syncConfiguration.isEnabled()) return;

        logger.debug("[LIVE-SYNC] Verificando partidos recien finalizados...");

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        // Buscar partidos finalizados en la ultima hora
        fixtureRepository.findByStatusShortIn(FINISHED_STATUSES.stream().toList())
            .filter(fixture -> fixture.date() != null && fixture.date().isAfter(oneHourAgo))
            .collectList()
            .flatMapMany(finishedFixtures -> {
                if (finishedFixtures.isEmpty()) {
                    logger.debug("[LIVE-SYNC] No hay partidos recien finalizados");
                    return Flux.empty();
                }

                logger.info("[LIVE-SYNC] Encontrados {} partidos recien finalizados", finishedFixtures.size());

                return Flux.fromIterable(finishedFixtures)
                    .flatMap(fixture -> {
                        logger.info("[LIVE-SYNC] Sincronizando datos completos del fixture {} ({} {} - {} {})",
                            fixture.apiFootballId(),
                            fixture.homeTeamName(),
                            fixture.goalsHome(),
                            fixture.goalsAway(),
                            fixture.awayTeamName());

                        // Sincronizar stats, events y lineups
                        return syncService.syncFullFixtureData(fixture.apiFootballId(), SyncTrigger.EVENT)
                            .doOnSuccess(result -> {
                                if (!result.wasSkipped()) {
                                    logger.info("[LIVE-SYNC] Datos completos sincronizados para fixture {}: {} registros",
                                        fixture.apiFootballId(), result.processed());
                                }
                            })
                            .onErrorResume(e -> {
                                logger.warn("[LIVE-SYNC] Error sincronizando datos del fixture {}: {}",
                                    fixture.apiFootballId(), e.getMessage());
                                return reactor.core.publisher.Mono.empty();
                            });
                    }, 1);  // Un fixture a la vez para datos completos
            })
            .subscribe();
    }

    /**
     * Sincroniza lineups de partidos proximos (1 hora antes)
     */
    @Scheduled(fixedDelayString = "${sync.lineup-polling.interval:900000}")  // Cada 15 minutos
    public void syncUpcomingLineups() {
        if (!syncConfiguration.isEnabled()) return;

        logger.debug("[LIVE-SYNC] Verificando lineups de partidos proximos...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursFromNow = now.plusHours(2);

        // Buscar partidos que empiezan en las proximas 2 horas
        fixtureRepository.findByDateBetween(now, twoHoursFromNow)
            .filter(fixture -> "NS".equals(fixture.statusShort()) || "TBD".equals(fixture.statusShort()))
            .collectList()
            .flatMapMany(upcomingFixtures -> {
                if (upcomingFixtures.isEmpty()) {
                    logger.debug("[LIVE-SYNC] No hay partidos proximos para sincronizar lineups");
                    return Flux.empty();
                }

                logger.info("[LIVE-SYNC] Encontrados {} partidos proximos para lineups", upcomingFixtures.size());

                return Flux.fromIterable(upcomingFixtures)
                    .flatMap(fixture -> {
                        logger.info("[LIVE-SYNC] Intentando obtener lineups del fixture {} ({} vs {}) - Hora: {}",
                            fixture.apiFootballId(),
                            fixture.homeTeamName(),
                            fixture.awayTeamName(),
                            fixture.date());

                        return syncService.syncMatchLineups(fixture.apiFootballId(), SyncTrigger.EVENT)
                            .doOnSuccess(result -> {
                                if (!result.wasSkipped() && result.processed() > 0) {
                                    logger.info("[LIVE-SYNC] Lineups sincronizados para fixture {}: {} jugadores",
                                        fixture.apiFootballId(), result.processed());
                                }
                            })
                            .onErrorResume(e -> {
                                // Es normal que no haya lineups disponibles aun
                                logger.debug("[LIVE-SYNC] Lineups no disponibles aun para fixture {}", fixture.apiFootballId());
                                return reactor.core.publisher.Mono.empty();
                            });
                    }, 2);
            })
            .subscribe();
    }
}