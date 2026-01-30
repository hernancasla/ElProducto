package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.infrastructure.adapter.repository.entity.CountryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para acceso reactivo a la tabla countries
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas
 */
@Repository
public interface CountryR2dbcRepository extends ReactiveCrudRepository<CountryEntity, Long> {

    /**
     * Busca un país por su código ISO
     * @param code Código ISO del país (ej: AR, BR, ES)
     * @return Mono con la entidad del país si existe
     */
    Mono<CountryEntity> findByCode(String code);

    /**
     * Busca un país por su nombre
     * @param name Nombre del país
     * @return Mono con la entidad del país si existe
     */
    Mono<CountryEntity> findByName(String name);

    /**
     * Elimina un país por su código ISO
     * @param code Código ISO del país
     * @return Mono que completa cuando la eliminación termina
     */
    Mono<Void> deleteByCode(String code);
}