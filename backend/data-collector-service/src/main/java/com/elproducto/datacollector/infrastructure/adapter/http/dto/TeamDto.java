package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear la información de equipo desde API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("code") String code,
    @JsonProperty("country") String country,
    @JsonProperty("founded") Integer founded,
    @JsonProperty("national") Boolean national,
    @JsonProperty("logo") String logo
) {
}