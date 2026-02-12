package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear status dentro de fixture
 *
 * Status codes:
 * - TBD: Time To Be Defined
 * - NS: Not Started
 * - 1H: First Half
 * - HT: Halftime
 * - 2H: Second Half
 * - ET: Extra Time
 * - BT: Break Time
 * - P: Penalty In Progress
 * - SUSP: Suspended
 * - INT: Interrupted
 * - FT: Match Finished
 * - AET: After Extra Time
 * - PEN: Penalty Shootout
 * - PST: Postponed
 * - CANC: Cancelled
 * - ABD: Abandoned
 * - AWD: Technical Loss
 * - WO: WalkOver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureStatusDto(
    @JsonProperty("long") String longStatus,
    @JsonProperty("short") String shortStatus,
    @JsonProperty("elapsed") Integer elapsed
) {}