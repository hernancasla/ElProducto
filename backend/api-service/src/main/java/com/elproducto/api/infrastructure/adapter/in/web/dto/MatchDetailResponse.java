package com.elproducto.api.infrastructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDetailResponse {
    private Long id;
    private LocalDateTime date;
    private String statusLong;
    private String statusShort;
    private Integer elapsed;
    private LeagueResponse league;
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;
    private ScoreResponse score;
    private String venueName;
    private String venueCity;
    private String refereeName;
}
