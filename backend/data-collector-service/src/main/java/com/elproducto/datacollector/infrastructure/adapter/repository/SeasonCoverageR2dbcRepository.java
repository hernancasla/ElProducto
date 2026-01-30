package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.SeasonCoverageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla season_coverage
 */
@Repository
public interface SeasonCoverageR2dbcRepository extends ReactiveCrudRepository<SeasonCoverageEntity, Long> {

    /**
     * Busca la cobertura de una temporada por su ID de temporada
     * @param seasonId ID de la temporada
     * @return Mono con la entidad de cobertura si existe
     */
    Mono<SeasonCoverageEntity> findBySeasonId(Long seasonId);

    /**
     * Elimina la cobertura de una temporada por su ID de temporada
     * @param seasonId ID de la temporada
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteBySeasonId(Long seasonId);
}