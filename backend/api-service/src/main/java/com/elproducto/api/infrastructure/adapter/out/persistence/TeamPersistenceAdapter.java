package com.elproducto.api.infrastructure.adapter.out.persistence;

import com.elproducto.api.domain.model.Team;
import com.elproducto.api.domain.port.out.TeamRepositoryPort;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.TeamJpaEntity;
import com.elproducto.api.infrastructure.adapter.out.persistence.mapper.TeamPersistenceMapper;
import com.elproducto.api.infrastructure.adapter.out.persistence.repository.TeamJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeamPersistenceAdapter implements TeamRepositoryPort {

    private final TeamJpaRepository teamJpaRepository;
    private final TeamPersistenceMapper teamPersistenceMapper;

    @Override
    public Page<Team> findAll(String country, String search, Pageable pageable) {
        Specification<TeamJpaEntity> spec = Specification.where(null);

        if (country != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("country"), country));
        }

        if (search != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
        }

        return teamJpaRepository.findAll(spec, pageable)
                .map(teamPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findById(id)
                .map(teamPersistenceMapper::toDomain);
    }
}
