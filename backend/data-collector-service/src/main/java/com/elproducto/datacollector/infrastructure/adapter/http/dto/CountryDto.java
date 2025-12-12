package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para deserializar respuesta de países de API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryDto(
    String name,
    String code,
    String flag
) {
}