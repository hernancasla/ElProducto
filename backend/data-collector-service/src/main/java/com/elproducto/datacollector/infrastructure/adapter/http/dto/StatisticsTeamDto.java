package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para información del equipo en estadísticas
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StatisticsTeamDto(
    Long id,
    String name,
    String logo
) {}