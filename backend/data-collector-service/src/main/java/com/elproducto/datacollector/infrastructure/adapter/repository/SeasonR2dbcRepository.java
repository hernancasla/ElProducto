package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.SeasonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla seasons
 */
@Repository
public interface SeasonR2dbcRepository extends ReactiveCrudRepository<SeasonEntity, Long> {

    /**
     * Busca todas las temporadas de una liga
     * @param leagueId ID de la liga
     * @return Flux con las entidades de temporadas
     */
    Flux<SeasonEntity> findByLeagueId(Long leagueId);

    /**
     * Busca una temporada específica por liga y año
     * @param leagueId ID de la liga
     * @param year Año de la temporada
     * @return Mono con la entidad de temporada si existe
     */
    Mono<SeasonEntity> findByLeagueIdAndYear(Long leagueId, Integer year);

    /**
     * Elimina todas las temporadas de una liga
     * @param leagueId ID de la liga
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByLeagueId(Long leagueId);
}