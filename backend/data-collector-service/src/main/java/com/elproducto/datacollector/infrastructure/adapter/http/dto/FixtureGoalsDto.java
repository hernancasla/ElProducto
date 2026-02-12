package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear goals dentro de fixture response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureGoalsDto(
    @JsonProperty("home") Integer home,
    @JsonProperty("away") Integer away
) {}