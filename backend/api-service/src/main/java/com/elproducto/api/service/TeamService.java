package com.elproducto.api.service;

import com.elproducto.api.dto.TeamDTO;
import com.elproducto.api.entity.Team;
import com.elproducto.api.exception.ResourceNotFoundException;
import com.elproducto.api.mapper.TeamMapper;
import com.elproducto.api.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    
    public Page<TeamDTO> findTeams(String country, String search, Pageable pageable) {
        Specification<Team> spec = Specification.where(null);
        
        if (country != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("country"), country));
        }
        
        if (search != null) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
        }
        
        Page<Team> teams = teamRepository.findAll(spec, pageable);
        return teams.map(teamMapper::toDTO);
    }
    
    @Cacheable(value = "team", key = "#id")
    public TeamDTO findById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
        return teamMapper.toDTO(team);
    }
}
