package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para deserializar la respuesta completa del endpoint /leagues de API-Football
 * Estructura: { "get": "leagues", "parameters": {...}, "errors": [], "results": 14, "paging": {...}, "response": [...] }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballLeaguesResponse(
    String get,
    Object parameters,
    Object errors,  // Cambiado de List<Object> a Object porque la API puede enviar {} o []
    Integer results,
    ApiFootballResponse.PagingDto paging,
    List<LeagueResponseDto> response
) {
}