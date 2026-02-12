package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * DTO para mapear la respuesta completa de la API de API-Football para standings
 *
 * Estructura de la respuesta:
 * {
 *   "get": "standings",
 *   "parameters": { "league": "128", "season": "2024" },
 *   "errors": [],
 *   "results": 1,
 *   "response": [ { "league": { ..., "standings": [[...]] } } ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballStandingsResponse(
    @JsonProperty("get") String get,
    @JsonProperty("parameters") Map<String, Object> parameters,
    @JsonProperty("errors") List<Object> errors,
    @JsonProperty("results") Integer results,
    @JsonProperty("response") List<StandingsLeagueResponseDto> response
) {
    /**
     * Verifica si la respuesta tiene errores
     */
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * Verifica si la respuesta tiene resultados
     */
    public boolean hasResults() {
        return response != null && !response.isEmpty();
    }
}