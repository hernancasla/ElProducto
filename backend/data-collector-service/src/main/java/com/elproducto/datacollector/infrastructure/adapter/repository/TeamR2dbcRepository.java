package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.TeamEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla teams
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface TeamR2dbcRepository extends ReactiveCrudRepository<TeamEntity, Long> {

    /**
     * Busca un equipo por su ID de API-Football
     * @param apiFootballId ID del equipo en API-Football
     * @return Mono con la entidad del equipo si existe
     */
    Mono<TeamEntity> findByApiFootballId(Long apiFootballId);

    /**
     * Busca equipos por país
     * @param country Nombre del país
     * @return Flux con los equipos del país
     */
    Flux<TeamEntity> findByCountry(String country);

    /**
     * Busca un equipo por su nombre
     * @param name Nombre del equipo
     * @return Mono con la entidad del equipo si existe
     */
    Mono<TeamEntity> findByName(String name);

    /**
     * Busca equipos que son selecciones nacionales
     * @param national true para buscar selecciones nacionales
     * @return Flux con los equipos nacionales
     */
    Flux<TeamEntity> findByNational(Boolean national);

    /**
     * Elimina un equipo por su ID de API-Football
     * @param apiFootballId ID del equipo en API-Football
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByApiFootballId(Long apiFootballId);
}