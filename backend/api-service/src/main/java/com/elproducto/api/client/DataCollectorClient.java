package com.elproducto.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * HTTP client for the data-collector-service.
 * Uses Spring RestClient (blocking) — appropriate for admin on-demand invocations.
 * All methods return the raw response body as a generic Map so the admin controller
 * can forward the JSON to the frontend without needing collector-specific DTOs.
 */
@Component
public class DataCollectorClient {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorClient.class);
    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {};

    private final RestClient restClient;

    public DataCollectorClient(@Value("${app.collector.url}") String collectorUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(collectorUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        logger.info("DataCollectorClient configured with base URL: {}", collectorUrl);
    }

    // ──────────────────────────────────────────────
    // Collector endpoints  /api/v1/collector/*
    // ──────────────────────────────────────────────

    public Map<String, Object> collectCountries() {
        logger.info("[collector] POST /countries");
        return postCollector("/api/v1/collector/countries", null);
    }

    public Map<String, Object> collectLeagues(String countryCode) {
        logger.info("[collector] POST /leagues?countryCode={}", countryCode);
        return postCollector("/api/v1/collector/leagues?countryCode={cc}", countryCode);
    }

    public Map<String, Object> collectTeams(String country) {
        logger.info("[collector] POST /teams?country={}", country);
        return postCollector("/api/v1/collector/teams?country={c}", country);
    }

    public Map<String, Object> collectPlayers(Long teamId, Integer season) {
        logger.info("[collector] POST /players?teamId={}&season={}", teamId, season);
        return restClient.post()
                .uri("/api/v1/collector/players?teamId={t}&season={s}", teamId, season)
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> collectFixtures(Long leagueId, Integer season) {
        logger.info("[collector] POST /fixtures?leagueId={}&season={}", leagueId, season);
        return restClient.post()
                .uri("/api/v1/collector/fixtures?leagueId={l}&season={s}", leagueId, season)
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> collectStandings(Long leagueId, Integer season) {
        logger.info("[collector] POST /standings?leagueId={}&season={}", leagueId, season);
        return restClient.post()
                .uri("/api/v1/collector/standings?leagueId={l}&season={s}", leagueId, season)
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> collectStatistics(Long fixtureId) {
        logger.info("[collector] POST /statistics?fixtureId={}", fixtureId);
        return restClient.post()
                .uri("/api/v1/collector/statistics?fixtureId={f}", fixtureId)
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> collectEvents(Long fixtureId) {
        logger.info("[collector] POST /events?fixtureId={}", fixtureId);
        return restClient.post()
                .uri("/api/v1/collector/events?fixtureId={f}", fixtureId)
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> collectLineups(Long fixtureId) {
        logger.info("[collector] POST /lineups?fixtureId={}", fixtureId);
        return restClient.post()
                .uri("/api/v1/collector/lineups?fixtureId={f}", fixtureId)
                .retrieve()
                .body(MAP_TYPE);
    }

    // ──────────────────────────────────────────────
    // Sync endpoints  /api/v1/sync/*
    // ──────────────────────────────────────────────

    public Map<String, Object> getRunningSyncs() {
        logger.info("[collector] GET /sync/running");
        return restClient.get()
                .uri("/api/v1/sync/running")
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> getSyncHistory(String entityType, int limit) {
        logger.info("[collector] GET /sync/history/{} limit={}", entityType, limit);
        return restClient.get()
                .uri("/api/v1/sync/history/{type}?limit={l}", entityType, limit)
                .retrieve()
                .body(MAP_TYPE);
    }

    public Map<String, Object> getSyncStats(String entityType) {
        logger.info("[collector] GET /sync/stats/{}", entityType);
        return restClient.get()
                .uri("/api/v1/sync/stats/{type}", entityType)
                .retrieve()
                .body(MAP_TYPE);
    }

    // ──────────────────────────────────────────────
    // Health check
    // ──────────────────────────────────────────────

    public boolean isHealthy() {
        try {
            var response = restClient.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .toBodilessEntity();
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            logger.warn("[collector] Health check failed: {}", e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

    private Map<String, Object> postCollector(String uriTemplate, String param) {
        var spec = restClient.post();
        var uriSpec = param != null
                ? spec.uri(uriTemplate, param)
                : spec.uri(uriTemplate);
        return uriSpec.retrieve().body(MAP_TYPE);
    }
}
