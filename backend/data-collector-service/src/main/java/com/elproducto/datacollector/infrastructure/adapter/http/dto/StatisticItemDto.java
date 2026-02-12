package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para un item de estadística individual
 * Ejemplo: {"type": "Shots on Goal", "value": 8}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StatisticItemDto(
    String type,
    Object value
) {}