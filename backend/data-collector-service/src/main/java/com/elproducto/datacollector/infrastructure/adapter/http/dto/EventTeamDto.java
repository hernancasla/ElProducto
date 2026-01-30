package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para información del equipo en un evento
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventTeamDto(
    Long id,
    String name,
    String logo
) {}