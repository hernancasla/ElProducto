package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para deserializar información de temporada de API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonDto(
    Integer year,
    String start,
    String end,
    boolean current,
    CoverageDto coverage
) {
}