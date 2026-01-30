package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para información del equipo en una alineación
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineupTeamDto(
    Long id,
    String name,
    String logo
) {}