package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.VenueEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla venues
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface VenueR2dbcRepository extends ReactiveCrudRepository<VenueEntity, Long> {

    /**
     * Busca un venue por su ID de API-Football
     * @param apiFootballId ID del venue en API-Football
     * @return Mono con la entidad del venue si existe
     */
    Mono<VenueEntity> findByApiFootballId(Long apiFootballId);

    /**
     * Busca un venue por su nombre
     * @param name Nombre del venue
     * @return Mono con la entidad del venue si existe
     */
    Mono<VenueEntity> findByName(String name);

    /**
     * Elimina un venue por su ID de API-Football
     * @param apiFootballId ID del venue en API-Football
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByApiFootballId(Long apiFootballId);
}