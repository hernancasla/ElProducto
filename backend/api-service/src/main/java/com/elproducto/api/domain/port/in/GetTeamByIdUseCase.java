package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.Team;

public interface GetTeamByIdUseCase {
    Team execute(Long id);
}
