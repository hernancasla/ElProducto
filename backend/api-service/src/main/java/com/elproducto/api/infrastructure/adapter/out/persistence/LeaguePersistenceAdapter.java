package com.elproducto.api.infrastructure.adapter.out.persistence;

import com.elproducto.api.domain.model.League;
import com.elproducto.api.domain.port.out.LeagueRepositoryPort;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.LeagueJpaEntity;
import com.elproducto.api.infrastructure.adapter.out.persistence.mapper.LeaguePersistenceMapper;
import com.elproducto.api.infrastructure.adapter.out.persistence.repository.LeagueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LeaguePersistenceAdapter implements LeagueRepositoryPort {

    private final LeagueJpaRepository leagueJpaRepository;
    private final LeaguePersistenceMapper leaguePersistenceMapper;

    @Override
    public Page<League> findAll(String country, Integer season, Pageable pageable) {
        Specification<LeagueJpaEntity> spec = Specification.where(null);

        if (country != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("country"), country));
        }

        if (season != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("season"), season));
        }

        return leagueJpaRepository.findAll(spec, pageable)
                .map(leaguePersistenceMapper::toDomain);
    }

    @Override
    public Optional<League> findById(Long id) {
        return leagueJpaRepository.findById(id)
                .map(leaguePersistenceMapper::toDomain);
    }
}
