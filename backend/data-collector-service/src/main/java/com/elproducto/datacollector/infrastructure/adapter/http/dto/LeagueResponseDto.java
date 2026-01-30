package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para deserializar cada elemento del array response de leagues de API-Football
 * Estructura: { "league": {...}, "country": {...}, "seasons": [...] }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueResponseDto(
    LeagueDto league,
    CountryDto country,
    List<SeasonDto> seasons
) {
}