package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para deserializar información de liga de API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueDto(
    Long id,
    String name,
    String type,
    String logo
) {
}