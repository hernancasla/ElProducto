package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.LeagueEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla leagues
 */
@Repository
public interface LeagueR2dbcRepository extends ReactiveCrudRepository<LeagueEntity, Long> {

    /**
     * Busca una liga por su ID de API-Football
     * @param apiFootballId ID de la liga en API-Football
     * @return Mono con la entidad de liga si existe
     */
    Mono<LeagueEntity> findByApiFootballId(Long apiFootballId);

    /**
     * Busca una liga por su nombre
     * @param name Nombre de la liga
     * @return Mono con la entidad de liga si existe
     */
    Mono<LeagueEntity> findByName(String name);

    /**
     * Busca todas las ligas de un país
     * @param countryCode Código ISO del país
     * @return Flux con las entidades de ligas
     */
    Flux<LeagueEntity> findByCountryCode(String countryCode);

    /**
     * Busca todas las ligas de un tipo específico
     * @param type Tipo de liga (League, Cup, etc.)
     * @return Flux con las entidades de ligas
     */
    Flux<LeagueEntity> findByType(String type);

    /**
     * Elimina una liga por su ID de API-Football
     * @param apiFootballId ID de la liga en API-Football
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByApiFootballId(Long apiFootballId);
}