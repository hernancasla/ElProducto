package com.elproducto.api.infrastructure.adapter.out.persistence.repository;

import com.elproducto.api.infrastructure.adapter.out.persistence.entity.LeagueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeagueJpaRepository extends JpaRepository<LeagueJpaEntity, Long>, JpaSpecificationExecutor<LeagueJpaEntity> {
    Optional<LeagueJpaEntity> findByApiId(Long apiId);
}
