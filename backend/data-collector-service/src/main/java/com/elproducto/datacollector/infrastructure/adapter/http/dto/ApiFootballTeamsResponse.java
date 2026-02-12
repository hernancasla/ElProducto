package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * DTO para mapear la respuesta completa de la API de API-Football para teams
 *
 * Estructura de la respuesta:
 * {
 *   "get": "teams",
 *   "parameters": { "country": "argentina" },
 *   "errors": [],
 *   "results": 271,
 *   "paging": { "current": 1, "total": 1 },
 *   "response": [ { "team": {...}, "venue": {...} }, ... ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballTeamsResponse(
    @JsonProperty("get") String get,
    @JsonProperty("parameters") Map<String, Object> parameters,
    @JsonProperty("errors") List<Object> errors,
    @JsonProperty("results") Integer results,
    @JsonProperty("paging") Map<String, Object> paging,
    @JsonProperty("response") List<TeamResponseDto> response
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