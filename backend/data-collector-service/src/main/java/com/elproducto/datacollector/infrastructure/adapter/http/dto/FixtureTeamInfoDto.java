package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear info de un equipo dentro de teams (home/away)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureTeamInfoDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("logo") String logo,
    @JsonProperty("winner") Boolean winner
) {}