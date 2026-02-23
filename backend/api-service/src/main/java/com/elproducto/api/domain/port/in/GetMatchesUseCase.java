package com.elproducto.api.domain.port.in;

import com.elproducto.api.domain.model.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GetMatchesUseCase {
    Page<Match> execute(Long leagueId, Long teamId, LocalDate date, String status, Pageable pageable);
}
