package com.elproducto.api.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Match {
    Long id;
    LocalDateTime date;
    Long timestamp;
    String timezone;
    League league;
    Team homeTeam;
    Team awayTeam;
    Integer homeGoals;
    Integer awayGoals;
    Integer halftimeHome;
    Integer halftimeAway;
    Integer fulltimeHome;
    Integer fulltimeAway;
    String statusLong;
    String statusShort;
    Integer elapsed;
    String venueName;
    String venueCity;
    String refereeName;
    Long apiId;
}
