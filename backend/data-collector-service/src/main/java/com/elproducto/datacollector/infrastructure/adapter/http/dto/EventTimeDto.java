package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para el tiempo de un evento
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventTimeDto(
    Integer elapsed,
    Integer extra
) {}