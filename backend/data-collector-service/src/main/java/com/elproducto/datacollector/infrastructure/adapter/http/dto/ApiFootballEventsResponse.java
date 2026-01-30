package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para la respuesta de eventos de partidos de API-Football
 * Endpoint: GET /fixtures/events?fixture={id}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballEventsResponse(
    String get,
    Object parameters,
    Object errors,
    Integer results,
    List<EventResponseDto> response
) {
    /**
     * Verifica si la respuesta contiene errores
     */
    public boolean hasErrors() {
        if (errors == null) return false;
        if (errors instanceof List) {
            return !((List<?>) errors).isEmpty();
        }
        if (errors instanceof java.util.Map) {
            return !((java.util.Map<?, ?>) errors).isEmpty();
        }
        return false;
    }
}