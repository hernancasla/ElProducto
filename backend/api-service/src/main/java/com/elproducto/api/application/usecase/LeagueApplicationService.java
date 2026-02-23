package com.elproducto.api.application.usecase;

import com.elproducto.api.domain.exception.ResourceNotFoundException;
import com.elproducto.api.domain.model.League;
import com.elproducto.api.domain.port.in.GetLeagueByIdUseCase;
import com.elproducto.api.domain.port.in.GetLeaguesUseCase;
import com.elproducto.api.domain.port.out.LeagueRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeagueApplicationService implements GetLeaguesUseCase, GetLeagueByIdUseCase {

    private final LeagueRepositoryPort leagueRepositoryPort;

    @Override
    public Page<League> execute(String country, Integer season, Pageable pageable) {
        return leagueRepositoryPort.findAll(country, season, pageable);
    }

    @Override
    @Cacheable(value = "league", key = "#id")
    public League execute(Long id) {
        return leagueRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("League", id));
    }
}
