package com.elproducto.datacollector.infrastructure.scheduler;

import com.elproducto.datacollector.application.service.SyncService;
import com.elproducto.datacollector.domain.model.SyncTrigger;
import com.elproducto.datacollector.infrastructure.config.SyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler para sincronizaciones automaticas
 * Solo se activa si sync.enabled=true en la configuracion
 */
@Component
@ConditionalOnProperty(name = "sync.enabled", havingValue = "true")
public class SyncScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SyncScheduler.class);

    private final SyncService syncService;
    private final SyncConfiguration syncConfiguration;

    // Configuracion para MVP - Liga Argentina
    private static final String COUNTRY_CODE = "AR";
    private static final String COUNTRY_NAME = "Argentina";
    private static final Long LIGA_PROFESIONAL_ID = 128L;
    private static final Integer CURRENT_SEASON = 2024;

    public SyncScheduler(SyncService syncService, SyncConfiguration syncConfiguration) {
        this.syncService = syncService;
        this.syncConfiguration = syncConfiguration;
        logger.info("[SCHEDULER] Scheduler de sincronizacion ACTIVADO");
    }

    /**
     * Sincronizacion de ligas - Domingos a medianoche
     * Cron: segundo minuto hora dia-mes mes dia-semana
     */
    @Scheduled(cron = "${sync.entities.leagues.cron:0 0 0 * * SUN}")
    public void syncLeagues() {
        if (!syncConfiguration.isEnabled()) return;

        logger.info("[SCHEDULER] Ejecutando sincronizacion programada de ligas");
        syncService.syncLeagues(COUNTRY_CODE, SyncTrigger.SCHEDULER)
            .doOnSuccess(result -> logger.info("[SCHEDULER] Ligas sincronizadas: {} procesados", result.processed()))
            .doOnError(error -> logger.error("[SCHEDULER] Error sincronizando ligas", error))
            .subscribe();
    }

    /**
     * Sincronizacion de equipos - Lunes a medianoche
     */
    @Scheduled(cron = "${sync.entities.teams.cron:0 0 0 * * MON}")
    public void syncTeams() {
        if (!syncConfiguration.isEnabled()) return;

        logger.info("[SCHEDULER] Ejecutando sincronizacion programada de equipos");
        syncService.syncTeams(COUNTRY_NAME, SyncTrigger.SCHEDULER)
            .doOnSuccess(result -> logger.info("[SCHEDULER] Equipos sincronizados: {} procesados", result.processed()))
            .doOnError(error -> logger.error("[SCHEDULER] Error sincronizando equipos", error))
            .subscribe();
    }

    /**
     * Sincronizacion de fixtures - Diario a las 6 AM
     */
    @Scheduled(cron = "${sync.entities.fixtures.cron:0 0 6 * * *}")
    public void syncFixtures() {
        if (!syncConfiguration.isEnabled()) return;

        logger.info("[SCHEDULER] Ejecutando sincronizacion programada de fixtures");
        syncService.syncFixtures(LIGA_PROFESIONAL_ID, CURRENT_SEASON, SyncTrigger.SCHEDULER)
            .doOnSuccess(result -> logger.info("[SCHEDULER] Fixtures sincronizados: {} procesados", result.processed()))
            .doOnError(error -> logger.error("[SCHEDULER] Error sincronizando fixtures", error))
            .subscribe();
    }

    /**
     * Sincronizacion de standings - Cada hora
     */
    @Scheduled(cron = "${sync.entities.standings.cron:0 0 * * * *}")
    public void syncStandings() {
        if (!syncConfiguration.isEnabled()) return;

        logger.info("[SCHEDULER] Ejecutando sincronizacion programada de standings");
        syncService.syncStandings(LIGA_PROFESIONAL_ID, CURRENT_SEASON, SyncTrigger.SCHEDULER)
            .doOnSuccess(result -> logger.info("[SCHEDULER] Standings sincronizados: {} procesados", result.processed()))
            .doOnError(error -> logger.error("[SCHEDULER] Error sincronizando standings", error))
            .subscribe();
    }

    /**
     * Limpieza de sincronizaciones antiguas - Cada 30 minutos
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void cleanupStaleSyncs() {
        if (!syncConfiguration.isEnabled()) return;

        logger.debug("[SCHEDULER] Limpiando sincronizaciones antiguas");
        syncService.cancelStaleSyncs()
            .doOnSuccess(count -> {
                if (count > 0) {
                    logger.warn("[SCHEDULER] {} sincronizaciones canceladas por timeout", count);
                }
            })
            .subscribe();
    }
}