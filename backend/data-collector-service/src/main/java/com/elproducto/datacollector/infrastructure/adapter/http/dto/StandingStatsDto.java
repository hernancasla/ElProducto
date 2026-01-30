package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear estadísticas (all/home/away) dentro de standing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingStatsDto(
    @JsonProperty("played") Integer played,
    @JsonProperty("win") Integer win,
    @JsonProperty("draw") Integer draw,
    @JsonProperty("lose") Integer lose,
    @JsonProperty("goals") StandingGoalsDto goals
) {}