package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear datos de un fixture desde API-Football
 *
 * Estructura:
 * {
 *   "id": 123456,
 *   "referee": "Referee Name",
 *   "timezone": "UTC",
 *   "date": "2024-01-15T15:00:00+00:00",
 *   "timestamp": 1705330800,
 *   "venue": { ... },
 *   "status": { ... }
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureInfoDto(
    @JsonProperty("id") Long id,
    @JsonProperty("referee") String referee,
    @JsonProperty("timezone") String timezone,
    @JsonProperty("date") String date,
    @JsonProperty("timestamp") Long timestamp,
    @JsonProperty("venue") FixtureVenueDto venue,
    @JsonProperty("status") FixtureStatusDto status
) {}