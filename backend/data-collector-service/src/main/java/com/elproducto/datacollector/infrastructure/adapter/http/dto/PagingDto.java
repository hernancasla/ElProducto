package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para información de paginación en respuestas de API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PagingDto(
    @JsonProperty("current") Integer current,
    @JsonProperty("total") Integer total
) {}