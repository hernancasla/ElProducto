package com.elproducto.api.infrastructure.adapter.out.persistence.repository;

import com.elproducto.api.infrastructure.adapter.out.persistence.entity.TeamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamJpaRepository extends JpaRepository<TeamJpaEntity, Long>, JpaSpecificationExecutor<TeamJpaEntity> {
    Optional<TeamJpaEntity> findByApiId(Long apiId);
}
