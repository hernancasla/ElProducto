package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear venue dentro de fixture
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureVenueDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("city") String city
) {}