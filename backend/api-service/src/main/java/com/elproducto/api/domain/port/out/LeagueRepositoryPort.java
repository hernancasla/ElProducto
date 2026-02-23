package com.elproducto.api.domain.port.out;

import com.elproducto.api.domain.model.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LeagueRepositoryPort {
    Page<League> findAll(String country, Integer season, Pageable pageable);
    Optional<League> findById(Long id);
}
