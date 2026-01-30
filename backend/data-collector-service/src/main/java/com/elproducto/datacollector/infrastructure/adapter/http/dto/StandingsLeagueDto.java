package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO para mapear la liga dentro de standings response
 * El campo standings es un array de arrays (para manejar grupos)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingsLeagueDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("country") String country,
    @JsonProperty("logo") String logo,
    @JsonProperty("flag") String flag,
    @JsonProperty("season") Integer season,
    @JsonProperty("standings") List<List<StandingEntryDto>> standings
) {}