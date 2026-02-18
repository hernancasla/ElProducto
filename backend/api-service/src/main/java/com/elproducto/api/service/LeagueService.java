package com.elproducto.api.service;

import com.elproducto.api.dto.LeagueDTO;
import com.elproducto.api.entity.League;
import com.elproducto.api.exception.ResourceNotFoundException;
import com.elproducto.api.mapper.LeagueMapper;
import com.elproducto.api.repository.LeagueRepository;
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
public class LeagueService {
    
    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;
    
    public Page<LeagueDTO> findLeagues(String country, Integer season, Pageable pageable) {
        Specification<League> spec = Specification.where(null);
        
        if (country != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("country"), country));
        }
        
        if (season != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("season"), season));
        }
        
        Page<League> leagues = leagueRepository.findAll(spec, pageable);
        return leagues.map(leagueMapper::toDTO);
    }
    
    @Cacheable(value = "league", key = "#id")
    public LeagueDTO findById(Long id) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("League", id));
        return leagueMapper.toDTO(league);
    }
}
