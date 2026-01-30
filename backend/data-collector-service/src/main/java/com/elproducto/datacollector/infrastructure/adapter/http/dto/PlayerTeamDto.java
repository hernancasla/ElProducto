package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear equipo dentro de estadísticas de jugador
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerTeamDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("logo") String logo
) {}