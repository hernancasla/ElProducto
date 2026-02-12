package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para deserializar información de cobertura de fixtures de API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixturesCoverageDto(
    boolean events,
    boolean lineups,
    @JsonProperty("statistics_fixtures") boolean statisticsFixtures,
    @JsonProperty("statistics_players") boolean statisticsPlayers
) {
}