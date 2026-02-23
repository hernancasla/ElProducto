package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.Match;

import java.util.List;

public interface GetMatchesByTeamUseCase {
    List<Match> execute(Long teamId, Integer limit);
}
