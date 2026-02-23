package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetLeaguesUseCase {
    Page<League> execute(String country, Integer season, Pageable pageable);
}
