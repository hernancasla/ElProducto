package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para información del entrenador en una alineación
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineupCoachDto(
    Long id,
    String name,
    String photo
) {}