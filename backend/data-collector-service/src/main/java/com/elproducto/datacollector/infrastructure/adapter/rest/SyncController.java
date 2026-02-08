package com.elproducto.datacollector.infrastructure.adapter.rest;

import com.elproducto.datacollector.application.service.SyncService;
import com.elproducto.datacollector.domain.model.EntityType;
import com.elproducto.datacollector.domain.model.SyncMetadata;
import com.elproducto.datacollector.domain.model.SyncTrigger;
import com.elproducto.datacollector.domain.port.SyncMetadataRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestion de sincronizaciones
 * Permite ver estado, historial y disparar syncs manuales
 */
@RestController
@RequestMapping("/api/v1/sync")
@Tag(name = "Sync Management", description = "Endpoints para gestion de sincronizaciones")
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    /**
     * Obtiene sincronizaciones en ejecucion
     */
    @Operation(summary = "Ver sincronizaciones en ejecucion")
    @GetMapping("/running")
    public Mono<ResponseEntity<SyncStatusResponse>> getRunning() {
        return syncService.getRunningSyncs()
            .collectList()
            .map(syncs -> {
                SyncStatusResponse response = new SyncStatusResponse(
                    "SUCCESS",
                    syncs.size() + " sincronizaciones en ejecucion",
                    syncs.stream().map(this::toSyncInfo).toList()
                );
                return ResponseEntity.ok(response);
            });
    }

    /**
     * Obtiene historial de sincronizaciones de una entidad
     */
    @Operation(summary = "Ver historial de sincronizaciones")
    @GetMapping("/history/{entityType}")
    public Mono<ResponseEntity<SyncStatusResponse>> getHistory(
            @PathVariable String entityType,
            @RequestParam(defaultValue = "20") int limit) {
        try {
            EntityType type = EntityType.valueOf(entityType.toUpperCase());
            return syncService.getSyncHistory(type, limit)
                .collectList()
                .map(syncs -> {
                    SyncStatusResponse response = new SyncStatusResponse(
                        "SUCCESS",
                        "Historial de " + entityType,
                        syncs.stream().map(this::toSyncInfo).toList()
                    );
                    return ResponseEntity.ok(response);
                });
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.badRequest().body(
                new SyncStatusResponse("ERROR", "Tipo de entidad invalido: " + entityType, List.of())
            ));
        }
    }

    /**
     * Obtiene estadisticas de sincronizacion de una entidad
     */
    @Operation(summary = "Ver estadisticas de sincronizacion")
    @GetMapping("/stats/{entityType}")
    public Mono<ResponseEntity<SyncStatsResponse>> getStats(@PathVariable String entityType) {
        try {
            EntityType type = EntityType.valueOf(entityType.toUpperCase());
            return syncService.getSyncStats(type)
                .map(stats -> {
                    SyncStatsResponse response = new SyncStatsResponse(
                        "SUCCESS",
                        stats.entityType().name(),
                        stats.totalSyncs(),
                        stats.successfulSyncs(),
                        stats.failedSyncs(),
                        stats.totalRecordsProcessed(),
                        stats.totalApiCalls(),
                        stats.avgDurationSeconds()
                    );
                    return ResponseEntity.ok(response);
                });
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.badRequest().body(
                new SyncStatsResponse("ERROR", entityType, 0, 0, 0, 0, 0, 0)
            ));
        }
    }

    /**
     * Obtiene la ultima sincronizacion de una entidad
     */
    @Operation(summary = "Ver ultima sincronizacion de una entidad")
    @GetMapping("/last/{entityType}")
    public Mono<ResponseEntity<SyncInfoResponse>> getLastSync(@PathVariable String entityType) {
        try {
            EntityType type = EntityType.valueOf(entityType.toUpperCase());
            return syncService.getLastSync(type)
                .map(meta -> ResponseEntity.ok(new SyncInfoResponse("SUCCESS", toSyncInfo(meta))))
                .defaultIfEmpty(ResponseEntity.ok(new SyncInfoResponse("SUCCESS", null)));
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.badRequest().body(
                new SyncInfoResponse("ERROR", null)
            ));
        }
    }

    // ==================== TRIGGERS MANUALES ====================

    /**
     * Dispara sincronizacion de paises manualmente
     */
    @Operation(summary = "Sincronizar paises manualmente")
    @PostMapping("/trigger/countries")
    public Mono<ResponseEntity<SyncTriggerResponse>> triggerCountries() {
        logger.info("[SYNC-API] Trigger manual: countries");
        return syncService.syncCountries(SyncTrigger.MANUAL)
            .map(result -> ResponseEntity.ok(new SyncTriggerResponse(
                "SUCCESS", "Sincronizacion completada", result.processed(), result.apiCalls()
            )))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(
                new SyncTriggerResponse("ERROR", e.getMessage(), 0, 0)
            )));
    }

    /**
     * Dispara sincronizacion de ligas manualmente
     */
    @Operation(summary = "Sincronizar ligas manualmente")
    @PostMapping("/trigger/leagues")
    public Mono<ResponseEntity<SyncTriggerResponse>> triggerLeagues(
            @RequestParam(defaultValue = "AR") String countryCode) {
        logger.info("[SYNC-API] Trigger manual: leagues for {}", countryCode);
        return syncService.syncLeagues(countryCode, SyncTrigger.MANUAL)
            .map(result -> ResponseEntity.ok(new SyncTriggerResponse(
                "SUCCESS", "Sincronizacion completada", result.processed(), result.apiCalls()
            )))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(
                new SyncTriggerResponse("ERROR", e.getMessage(), 0, 0)
            )));
    }

    /**
     * Dispara sincronizacion de equipos manualmente
     */
    @Operation(summary = "Sincronizar equipos manualmente")
    @PostMapping("/trigger/teams")
    public Mono<ResponseEntity<SyncTriggerResponse>> triggerTeams(
            @RequestParam(defaultValue = "Argentina") String country) {
        logger.info("[SYNC-API] Trigger manual: teams for {}", country);
        return syncService.syncTeams(country, SyncTrigger.MANUAL)
            .map(result -> ResponseEntity.ok(new SyncTriggerResponse(
                "SUCCESS", "Sincronizacion completada", result.processed(), result.apiCalls()
            )))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(
                new SyncTriggerResponse("ERROR", e.getMessage(), 0, 0)
            )));
    }

    /**
     * Dispara sincronizacion de fixtures manualmente
     */
    @Operation(summary = "Sincronizar fixtures manualmente")
    @PostMapping("/trigger/fixtures")
    public Mono<ResponseEntity<SyncTriggerResponse>> triggerFixtures(
            @RequestParam Long leagueId,
            @RequestParam(defaultValue = "2024") Integer season) {
        logger.info("[SYNC-API] Trigger manual: fixtures for league {} season {}", leagueId, season);
        return syncService.syncFixtures(leagueId, season, SyncTrigger.MANUAL)
            .map(result -> ResponseEntity.ok(new SyncTriggerResponse(
                "SUCCESS", "Sincronizacion completada", result.processed(), result.apiCalls()
            )))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(
                new SyncTriggerResponse("ERROR", e.getMessage(), 0, 0)
            )));
    }

    /**
     * Dispara sincronizacion de standings manualmente
     */
    @Operation(summary = "Sincronizar standings manualmente")
    @PostMapping("/trigger/standings")
    public Mono<ResponseEntity<SyncTriggerResponse>> triggerStandings(
            @RequestParam Long leagueId,
            @RequestParam(defaultValue = "2024") Integer season) {
        logger.info("[SYNC-API] Trigger manual: standings for league {} season {}", leagueId, season);
        return syncService.syncStandings(leagueId, season, SyncTrigger.MANUAL)
            .map(result -> ResponseEntity.ok(new SyncTriggerResponse(
                "SUCCESS", "Sincronizacion completada", result.processed(), result.apiCalls()
            )))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(
                new SyncTriggerResponse("ERROR", e.getMessage(), 0, 0)
            )));
    }

    /**
     * Dispara sincronizacion completa de un fixture manualmente
     */
    @Operation(summary = "Sincronizar datos completos de un fixture")
    @PostMapping("/trigger/fixture/{fixtureId}")
    public Mono<ResponseEntity<SyncTriggerResponse>> triggerFixtureData(@PathVariable Long fixtureId) {
        logger.info("[SYNC-API] Trigger manual: full fixture data for {}", fixtureId);
        return syncService.syncFullFixtureData(fixtureId, SyncTrigger.MANUAL)
            .map(result -> ResponseEntity.ok(new SyncTriggerResponse(
                "SUCCESS", "Sincronizacion completada (stats + events + lineups)",
                result.processed(), result.apiCalls()
            )))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(
                new SyncTriggerResponse("ERROR", e.getMessage(), 0, 0)
            )));
    }

    // ==================== DTOs ====================

    private SyncInfo toSyncInfo(SyncMetadata meta) {
        return new SyncInfo(
            meta.id(),
            meta.entityType().name(),
            meta.syncScope(),
            meta.status().name(),
            meta.strategy().name(),
            meta.startedAt(),
            meta.completedAt(),
            meta.recordsProcessed(),
            meta.recordsCreated(),
            meta.recordsUpdated(),
            meta.apiCallsMade(),
            meta.triggeredBy().name(),
            meta.errorMessage(),
            meta.getDuration().toSeconds()
        );
    }

    public record SyncStatusResponse(
        String status,
        String message,
        List<SyncInfo> syncs
    ) {}

    public record SyncInfoResponse(
        String status,
        SyncInfo sync
    ) {}

    public record SyncInfo(
        Long id,
        String entityType,
        String scope,
        String syncStatus,
        String strategy,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        int recordsProcessed,
        int recordsCreated,
        int recordsUpdated,
        int apiCalls,
        String triggeredBy,
        String errorMessage,
        long durationSeconds
    ) {}

    public record SyncStatsResponse(
        String status,
        String entityType,
        long totalSyncs,
        long successfulSyncs,
        long failedSyncs,
        long totalRecordsProcessed,
        long totalApiCalls,
        double avgDurationSeconds
    ) {}

    public record SyncTriggerResponse(
        String status,
        String message,
        int recordsProcessed,
        int apiCalls
    ) {}
}