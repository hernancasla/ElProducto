package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.League;

public interface GetLeagueByIdUseCase {
    League execute(Long id);
}
