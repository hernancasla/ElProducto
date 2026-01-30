package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear score dentro de fixture response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureScoreDto(
    @JsonProperty("halftime") FixtureGoalsDto halftime,
    @JsonProperty("fulltime") FixtureGoalsDto fulltime,
    @JsonProperty("extratime") FixtureGoalsDto extratime,
    @JsonProperty("penalty") FixtureGoalsDto penalty
) {}