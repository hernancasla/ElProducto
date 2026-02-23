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
public class MatchResponse {
    private Long id;
    private LocalDateTime date;
    private String status;
    private Integer elapsed;
    private LeagueResponse league;
    private TeamSummaryResponse homeTeam;
    private TeamSummaryResponse awayTeam;
    private ScoreResponse score;
}
