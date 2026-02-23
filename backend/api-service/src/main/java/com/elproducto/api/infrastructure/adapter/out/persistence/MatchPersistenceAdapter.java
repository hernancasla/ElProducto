package com.elproducto.api.infrastructure.adapter.out.persistence;

import com.elproducto.api.domain.model.Match;
import com.elproducto.api.domain.port.out.MatchRepositoryPort;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.MatchJpaEntity;
import com.elproducto.api.infrastructure.adapter.out.persistence.mapper.MatchPersistenceMapper;
import com.elproducto.api.infrastructure.adapter.out.persistence.repository.MatchJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MatchPersistenceAdapter implements MatchRepositoryPort {

    private final MatchJpaRepository matchJpaRepository;
    private final MatchPersistenceMapper matchPersistenceMapper;

    @Override
    public Page<Match> findAll(Long leagueId, Long teamId, LocalDate date, String status, Pageable pageable) {
        Specification<MatchJpaEntity> spec = Specification.where(null);

        if (leagueId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("league").get("id"), leagueId));
        }

        if (teamId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.equal(root.get("homeTeam").get("id"), teamId),
                            cb.equal(root.get("awayTeam").get("id"), teamId)
                    ));
        }

        if (date != null) {
            spec = spec.and((root, query, cb) -> {
                var startOfDay = date.atStartOfDay();
                var endOfDay = date.atTime(23, 59, 59);
                return cb.between(root.get("date"), startOfDay, endOfDay);
            });
        }

        if (status != null) {
            if ("live".equalsIgnoreCase(status)) {
                spec = spec.and((root, query, cb) ->
                        root.get("statusShort").in("1H", "2H", "HT", "ET", "P", "LIVE"));
            } else if ("finished".equalsIgnoreCase(status)) {
                spec = spec.and((root, query, cb) ->
                        root.get("statusShort").in("FT", "AET", "PEN"));
            } else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("statusShort"), status));
            }
        }

        return matchJpaRepository.findAll(spec, pageable)
                .map(matchPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Match> findById(Long id) {
        return matchJpaRepository.findById(id)
                .map(matchPersistenceMapper::toDomain);
    }

    @Override
    public List<Match> findLiveMatches() {
        return matchJpaRepository.findLiveMatches().stream()
                .map(matchPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> findByTeamId(Long teamId) {
        Specification<MatchJpaEntity> spec = (root, query, cb) ->
                cb.or(
                        cb.equal(root.get("homeTeam").get("id"), teamId),
                        cb.equal(root.get("awayTeam").get("id"), teamId)
                );

        return matchJpaRepository.findAll(spec).stream()
                .map(matchPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
