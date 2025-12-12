package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para deserializar la respuesta completa de API-Football
 * Estructura: { "response": [ {...}, {...} ], "errors": [], ... }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballResponse(
    List<CountryDto> response,
    List<String> errors
) {
}