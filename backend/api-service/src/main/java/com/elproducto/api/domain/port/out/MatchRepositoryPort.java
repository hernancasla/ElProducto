package com.elproducto.api.domain.port.out;

import com.elproducto.api.domain.model.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchRepositoryPort {
    Page<Match> findAll(Long leagueId, Long teamId, LocalDate date, String status, Pageable pageable);
    Optional<Match> findById(Long id);
    List<Match> findLiveMatches();
    List<Match> findByTeamId(Long teamId);
}
