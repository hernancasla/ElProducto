package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para deserializar la respuesta completa de API-Football
 * Estructura: { "get": "countries", "parameters": [], "errors": [], "results": 171, "paging": {...}, "response": [...] }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballResponse(
    String get,
    List<Object> parameters,
    List<Object> errors,
    Integer results,
    PagingDto paging,
    List<CountryDto> response
) {
    /**
     * DTO para información de paginación
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PagingDto(
        Integer current,
        Integer total
    ) {
    }
}