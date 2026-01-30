package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear liga dentro de estadísticas de jugador
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerLeagueDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("country") String country,
    @JsonProperty("logo") String logo,
    @JsonProperty("season") Integer season
) {}