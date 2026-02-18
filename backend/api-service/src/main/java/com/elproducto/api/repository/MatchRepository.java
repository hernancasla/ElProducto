package com.elproducto.api.repository;

import com.elproducto.api.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match> {
    
    Optional<Match> findByApiId(Long apiId);
    
    @Query("SELECT m FROM Match m WHERE m.statusShort IN ('1H', '2H', 'HT', 'ET', 'P', 'LIVE') ORDER BY m.date ASC")
    List<Match> findLiveMatches();
    
    List<Match> findByStatusShortIn(List<String> statuses);
}
