package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear cada elemento del array response de standings
 * Contiene la información de la liga con sus standings
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingsLeagueResponseDto(
    @JsonProperty("league") StandingsLeagueDto league
) {}
