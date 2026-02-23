package com.elproducto.api.infrastructure.adapter.out.persistence.repository;

import com.elproducto.api.infrastructure.adapter.out.persistence.entity.MatchJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchJpaRepository extends JpaRepository<MatchJpaEntity, Long>, JpaSpecificationExecutor<MatchJpaEntity> {

    Optional<MatchJpaEntity> findByApiId(Long apiId);

    @Query("SELECT m FROM MatchJpaEntity m WHERE m.statusShort IN ('1H', '2H', 'HT', 'ET', 'P', 'LIVE') ORDER BY m.date ASC")
    List<MatchJpaEntity> findLiveMatches();

    List<MatchJpaEntity> findByStatusShortIn(List<String> statuses);
}
