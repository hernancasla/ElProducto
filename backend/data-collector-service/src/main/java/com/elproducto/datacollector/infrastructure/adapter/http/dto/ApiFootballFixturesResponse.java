package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * DTO para mapear la respuesta completa de la API de API-Football para fixtures
 *
 * Estructura de la respuesta:
 * {
 *   "get": "fixtures",
 *   "parameters": { "league": "128", "season": "2024" },
 *   "errors": [],
 *   "results": 380,
 *   "paging": { "current": 1, "total": 1 },
 *   "response": [ { "fixture": {...}, "league": {...}, "teams": {...}, "goals": {...}, "score": {...} }, ... ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballFixturesResponse(
    @JsonProperty("get") String get,
    @JsonProperty("parameters") Map<String, Object> parameters,
    @JsonProperty("errors") List<Object> errors,
    @JsonProperty("results") Integer results,
    @JsonProperty("paging") PagingDto paging,
    @JsonProperty("response") List<FixtureResponseDto> response
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

    /**
     * Verifica si hay más páginas disponibles
     */
    public boolean hasMorePages() {
        return paging != null && paging.current() < paging.total();
    }
}