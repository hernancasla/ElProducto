package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear estadísticas de un jugador
 * Solo capturamos team y league por ahora, las stats detalladas se pueden agregar después
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerStatisticsDto(
    @JsonProperty("team") PlayerTeamDto team,
    @JsonProperty("league") PlayerLeagueDto league
) {}