package com.elproducto.api.application.usecase;

import com.elproducto.api.domain.exception.ResourceNotFoundException;
import com.elproducto.api.domain.model.Team;
import com.elproducto.api.domain.port.in.GetTeamByIdUseCase;
import com.elproducto.api.domain.port.in.GetTeamsUseCase;
import com.elproducto.api.domain.port.out.TeamRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamApplicationService implements GetTeamsUseCase, GetTeamByIdUseCase {

    private final TeamRepositoryPort teamRepositoryPort;

    @Override
    public Page<Team> execute(String country, String search, Pageable pageable) {
        return teamRepositoryPort.findAll(country, search, pageable);
    }

    @Override
    @Cacheable(value = "team", key = "#id")
    public Team execute(Long id) {
        return teamRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
    }
}
