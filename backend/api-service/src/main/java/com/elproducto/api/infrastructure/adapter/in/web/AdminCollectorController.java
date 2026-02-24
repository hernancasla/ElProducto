package com.elproducto.api.controller;

import com.elproducto.api.client.DataCollectorClient;
import com.elproducto.api.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Admin endpoints that proxy data-collection requests to the data-collector-service.
 * All endpoints are protected by the ApiKeyAuthFilter (X-Admin-Api-Key header).
 *
 * <p>The api-service acts as a gateway so that the frontend never needs to know the
 * collector's internal address — and so the collector can be on a private network.</p>
 */
@RestController
@RequestMapping("/api/v1/admin/collector")
@Tag(name = "Admin - Data Collector", description = "Proxy para invocar el data-collector-service a demanda")
@SecurityRequirement(name = "AdminApiKey")
public class AdminCollectorController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCollectorController.class);

    private final DataCollectorClient collectorClient;

    public AdminCollectorController(DataCollectorClient collectorClient) {
        this.collectorClient = collectorClient;
    }

    // ──────────────────────────────────────────────────
    // Status / Health
    // ──────────────────────────────────────────────────

    @Operation(summary = "Estado del data-collector-service")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus() {
        boolean healthy = collectorClient.isHealthy();
        Map<String, Object> data = Map.of(
                "online", healthy,
                "message", healthy ? "Collector disponible" : "Collector no disponible"
        );
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    // ──────────────────────────────────────────────────
    // Collection endpoints
    // ──────────────────────────────────────────────────

    @Operation(summary = "Recolectar países")
    @PostMapping("/collect/countries")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectCountries() {
        return proxy(() -> collectorClient.collectCountries());
    }

    @Operation(summary = "Recolectar ligas de un país")
    @PostMapping("/collect/leagues")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectLeagues(
            @RequestParam String countryCode) {
        return proxy(() -> collectorClient.collectLeagues(countryCode));
    }

    @Operation(summary = "Recolectar equipos de un país")
    @PostMapping("/collect/teams")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectTeams(
            @RequestParam String country) {
        return proxy(() -> collectorClient.collectTeams(country));
    }

    @Operation(summary = "Recolectar jugadores de un equipo y temporada")
    @PostMapping("/collect/players")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectPlayers(
            @RequestParam Long teamId,
            @RequestParam Integer season) {
        return proxy(() -> collectorClient.collectPlayers(teamId, season));
    }

    @Operation(summary = "Recolectar partidos de una liga y temporada")
    @PostMapping("/collect/fixtures")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectFixtures(
            @RequestParam Long leagueId,
            @RequestParam Integer season) {
        return proxy(() -> collectorClient.collectFixtures(leagueId, season));
    }

    @Operation(summary = "Recolectar tabla de posiciones de una liga y temporada")
    @PostMapping("/collect/standings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectStandings(
            @RequestParam Long leagueId,
            @RequestParam Integer season) {
        return proxy(() -> collectorClient.collectStandings(leagueId, season));
    }

    @Operation(summary = "Recolectar estadísticas de un partido")
    @PostMapping("/collect/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectStatistics(
            @RequestParam Long fixtureId) {
        return proxy(() -> collectorClient.collectStatistics(fixtureId));
    }

    @Operation(summary = "Recolectar eventos de un partido")
    @PostMapping("/collect/events")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectEvents(
            @RequestParam Long fixtureId) {
        return proxy(() -> collectorClient.collectEvents(fixtureId));
    }

    @Operation(summary = "Recolectar alineaciones de un partido")
    @PostMapping("/collect/lineups")
    public ResponseEntity<ApiResponse<Map<String, Object>>> collectLineups(
            @RequestParam Long fixtureId) {
        return proxy(() -> collectorClient.collectLineups(fixtureId));
    }

    // ──────────────────────────────────────────────────
    // Sync status endpoints
    // ──────────────────────────────────────────────────

    @Operation(summary = "Ver sincronizaciones en ejecución")
    @GetMapping("/sync/running")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRunningSyncs() {
        return proxy(() -> collectorClient.getRunningSyncs());
    }

    @Operation(summary = "Ver historial de sincronizaciones de una entidad")
    @GetMapping("/sync/history/{entityType}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncHistory(
            @PathVariable String entityType,
            @RequestParam(defaultValue = "20") int limit) {
        return proxy(() -> collectorClient.getSyncHistory(entityType, limit));
    }

    @Operation(summary = "Ver estadísticas de sincronización de una entidad")
    @GetMapping("/sync/stats/{entityType}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncStats(
            @PathVariable String entityType) {
        return proxy(() -> collectorClient.getSyncStats(entityType));
    }

    // ──────────────────────────────────────────────────
    // Helper
    // ──────────────────────────────────────────────────

    private ResponseEntity<ApiResponse<Map<String, Object>>> proxy(CollectorCall call) {
        try {
            Map<String, Object> result = call.execute();
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (RestClientException e) {
            logger.error("[admin-collector] Error calling collector: {}", e.getMessage());
            return ResponseEntity.status(502).body(
                    ApiResponse.error("Error al conectar con el data-collector-service: " + e.getMessage())
            );
        } catch (Exception e) {
            logger.error("[admin-collector] Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Error inesperado: " + e.getMessage())
            );
        }
    }

    @FunctionalInterface
    private interface CollectorCall {
        Map<String, Object> execute();
    }
}
