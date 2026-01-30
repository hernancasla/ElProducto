package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear league dentro de fixture response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureLeagueDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("country") String country,
    @JsonProperty("logo") String logo,
    @JsonProperty("flag") String flag,
    @JsonProperty("season") Integer season,
    @JsonProperty("round") String round
) {}