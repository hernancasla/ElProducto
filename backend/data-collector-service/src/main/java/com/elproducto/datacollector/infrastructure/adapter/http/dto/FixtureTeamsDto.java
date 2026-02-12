package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear teams dentro de fixture response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureTeamsDto(
    @JsonProperty("home") FixtureTeamInfoDto home,
    @JsonProperty("away") FixtureTeamInfoDto away
) {}