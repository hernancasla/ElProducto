package com.elproducto.api.infrastructure.adapter.in.web.mapper;

import com.elproducto.api.domain.model.Match;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchDetailResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.ScoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {LeagueWebMapper.class, TeamWebMapper.class})
public interface MatchWebMapper {

    @Mapping(target = "status", source = "statusShort")
    @Mapping(target = "score", expression = "java(mapScore(match))")
    MatchResponse toResponse(Match match);

    @Mapping(target = "score", expression = "java(mapScore(match))")
    MatchDetailResponse toDetailResponse(Match match);

    default ScoreResponse mapScore(Match match) {
        return ScoreResponse.builder()
                .home(match.getHomeGoals())
                .away(match.getAwayGoals())
                .halftime(ScoreResponse.HalftimeScore.builder()
                        .home(match.getHalftimeHome())
                        .away(match.getHalftimeAway())
                        .build())
                .fulltime(ScoreResponse.FulltimeScore.builder()
                        .home(match.getFulltimeHome())
                        .away(match.getFulltimeAway())
                        .build())
                .build();
    }
}
