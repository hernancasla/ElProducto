package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para deserializar información de cobertura de temporada de API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CoverageDto(
    FixturesCoverageDto fixtures,
    boolean standings,
    boolean players,
    @JsonProperty("top_scorers") boolean topScorers,
    @JsonProperty("top_assists") boolean topAssists,
    @JsonProperty("top_cards") boolean topCards,
    boolean injuries,
    boolean predictions,
    boolean odds
) {
}