package com.elproducto.api.mapper;

import com.elproducto.api.dto.MatchDTO;
import com.elproducto.api.dto.MatchDetailDTO;
import com.elproducto.api.dto.ScoreDTO;
import com.elproducto.api.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {LeagueMapper.class, TeamMapper.class})
public interface MatchMapper {
    
    @Mapping(target = "status", source = "statusShort")
    @Mapping(target = "score", expression = "java(mapScore(match))")
    @Mapping(target = "homeTeam", source = "homeTeam")
    @Mapping(target = "awayTeam", source = "awayTeam")
    @Mapping(target = "league", source = "league")
    MatchDTO toDTO(Match match);
    
    @Mapping(target = "score", expression = "java(mapScore(match))")
    MatchDetailDTO toDetailDTO(Match match);
    
    default ScoreDTO mapScore(Match match) {
        return ScoreDTO.builder()
                .home(match.getHomeGoals())
                .away(match.getAwayGoals())
                .halftime(ScoreDTO.HalftimeScoreDTO.builder()
                        .home(match.getHalftimeHome())
                        .away(match.getHalftimeAway())
                        .build())
                .fulltime(ScoreDTO.FulltimeScoreDTO.builder()
                        .home(match.getFulltimeHome())
                        .away(match.getFulltimeAway())
                        .build())
                .build();
    }
}
