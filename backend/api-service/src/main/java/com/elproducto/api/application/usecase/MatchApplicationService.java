package com.elproducto.api.application.usecase;

import com.elproducto.api.domain.exception.ResourceNotFoundException;
import com.elproducto.api.domain.model.Match;
import com.elproducto.api.domain.port.in.GetLiveMatchesUseCase;
import com.elproducto.api.domain.port.in.GetMatchByIdUseCase;
import com.elproducto.api.domain.port.in.GetMatchesByTeamUseCase;
import com.elproducto.api.domain.port.in.GetMatchesUseCase;
import com.elproducto.api.domain.port.out.MatchRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchApplicationService implements GetMatchesUseCase, GetMatchByIdUseCase, GetLiveMatchesUseCase, GetMatchesByTeamUseCase {

    private final MatchRepositoryPort matchRepositoryPort;

    @Override
    public Page<Match> execute(Long leagueId, Long teamId, LocalDate date, String status, Pageable pageable) {
        return matchRepositoryPort.findAll(leagueId, teamId, date, status, pageable);
    }

    @Override
    @Cacheable(value = "match", key = "#id")
    public Match execute(Long id) {
        return matchRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
    }

    @Override
    @Cacheable(value = "liveMatches", unless = "#result.isEmpty()")
    public List<Match> execute() {
        log.info("Fetching live matches from database");
        return matchRepositoryPort.findLiveMatches();
    }

    @Override
    public List<Match> execute(Long teamId, Integer limit) {
        return matchRepositoryPort.findByTeamId(teamId).stream()
                .limit(limit != null ? limit : 10)
                .toList();
    }
}
