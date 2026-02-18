package com.elproducto.api.service;

import com.elproducto.api.dto.MatchDTO;
import com.elproducto.api.dto.MatchDetailDTO;
import com.elproducto.api.entity.Match;
import com.elproducto.api.exception.ResourceNotFoundException;
import com.elproducto.api.mapper.MatchMapper;
import com.elproducto.api.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchService {
    
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    
    public Page<MatchDTO> findMatches(Long leagueId, Long teamId, LocalDate date, String status, Pageable pageable) {
        Specification<Match> spec = Specification.where(null);
        
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
        
        Page<Match> matches = matchRepository.findAll(spec, pageable);
        return matches.map(matchMapper::toDTO);
    }
    
    @Cacheable(value = "liveMatches", unless = "#result.isEmpty()")
    public List<MatchDTO> findLiveMatches() {
        log.info("Fetching live matches from database");
        List<Match> liveMatches = matchRepository.findLiveMatches();
        return liveMatches.stream()
                .map(matchMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = "match", key = "#id")
    public MatchDetailDTO findById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
        return matchMapper.toDetailDTO(match);
    }
    
    public List<MatchDTO> findMatchesByTeam(Long teamId, Integer limit) {
        Specification<Match> spec = (root, query, cb) -> 
            cb.or(
                cb.equal(root.get("homeTeam").get("id"), teamId),
                cb.equal(root.get("awayTeam").get("id"), teamId)
            );
        
        List<Match> matches = matchRepository.findAll(spec);
        return matches.stream()
                .limit(limit != null ? limit : 10)
                .map(matchMapper::toDTO)
                .collect(Collectors.toList());
    }
}
