package com.elproducto.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Long id;
    private LocalDateTime date;
    private String status;
    private Integer elapsed;
    private LeagueDTO league;
    private TeamSummaryDTO homeTeam;
    private TeamSummaryDTO awayTeam;
    private ScoreDTO score;
}
