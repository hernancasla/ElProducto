package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO wrapper para un jugador en la alineación
 * La API devuelve: { "player": { ... } }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineupPlayerWrapperDto(
    LineupPlayerDto player
) {}