package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear goles dentro de stats de standing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingGoalsDto(
    @JsonProperty("for") Integer goalsFor,
    @JsonProperty("against") Integer goalsAgainst
) {}