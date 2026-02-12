package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para información del jugador en una alineación
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineupPlayerDto(
    Long id,
    String name,
    Integer number,
    String pos,    // "G", "D", "M", "F"
    String grid    // Posición en el campo (ej: "1:1", "2:3")
) {}