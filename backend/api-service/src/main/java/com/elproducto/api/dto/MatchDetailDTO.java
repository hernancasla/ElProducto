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
public class MatchDetailDTO {
    private Long id;
    private LocalDateTime date;
    private String statusLong;
    private String statusShort;
    private Integer elapsed;
    private LeagueDTO league;
    private TeamDTO homeTeam;
    private TeamDTO awayTeam;
    private ScoreDTO score;
    private String venueName;
    private String venueCity;
    private String refereeName;
}
