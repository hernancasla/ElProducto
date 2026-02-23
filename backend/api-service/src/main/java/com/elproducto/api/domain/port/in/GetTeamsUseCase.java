package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetTeamsUseCase {
    Page<Team> execute(String country, String search, Pageable pageable);
}
