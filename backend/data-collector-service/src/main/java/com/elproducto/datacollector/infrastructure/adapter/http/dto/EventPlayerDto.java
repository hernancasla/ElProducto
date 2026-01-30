package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para información del jugador en un evento
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventPlayerDto(
    Long id,
    String name
) {}