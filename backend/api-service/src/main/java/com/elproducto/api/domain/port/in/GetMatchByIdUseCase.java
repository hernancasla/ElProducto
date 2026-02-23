package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.Match;

public interface GetMatchByIdUseCase {
    Match execute(Long id);
}
