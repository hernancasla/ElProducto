package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear datos de nacimiento de un jugador
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerBirthDto(
    @JsonProperty("date") String date,
    @JsonProperty("place") String place,
    @JsonProperty("country") String country
) {}