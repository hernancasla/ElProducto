package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.port.CountryRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.CountryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adaptador de repositorio para PostgreSQL usando R2DBC
 * Implementa el puerto CountryRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary  // Esta implementación tiene prioridad sobre InMemoryCountryRepository
public class PostgresCountryRepositoryAdapter implements CountryRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresCountryRepositoryAdapter.class);

    private final CountryR2dbcRepository r2dbcRepository;

    public PostgresCountryRepositoryAdapter(CountryR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<List<Country>> saveAll(List<Country> countries) {
        logger.info("💾 [POSTGRES] Guardando {} países en PostgreSQL", countries.size());

        return Flux.fromIterable(countries)
                .flatMap(country -> {
                    // Validar que el país tenga código (requerido por la BD)
                    if (country.code() == null || country.code().isBlank()) {
                        logger.warn("⚠️ [POSTGRES] País '{}' sin código, será omitido", country.name());
                        return Mono.empty();
                    }

                    // Buscar si ya existe por código
                    return r2dbcRepository.findByCode(country.code())
                        .doOnNext(existing -> {
                            // Marcar como entidad existente para que R2DBC haga UPDATE
                            existing.markAsNotNew();
                            logger.debug("💾 [POSTGRES] País {} ya existe (ID: {}), actualizando...",
                                country.code(), existing.getId());
                        })
                        .flatMap(existing -> {
                            // Si existe, actualizar
                            existing.setName(country.name());
                            existing.setFlag(country.flag());
                            existing.setUpdatedAt(LocalDateTime.now());
                            return r2dbcRepository.save(existing);
                        })
                        .switchIfEmpty(
                            // Si no existe, crear nuevo
                            Mono.defer(() -> {
                                logger.debug("💾 [POSTGRES] País {} es nuevo, creando...", country.code());
                                return r2dbcRepository.save(CountryEntity.fromDomain(country));
                            })
                        );
                })
                .map(CountryEntity::toDomain)
                .collectList()
                .doOnSuccess(savedCountries ->
                    logger.info("💾 [POSTGRES] {} países guardados exitosamente", savedCountries.size())
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando países", error)
                );
    }

    @Override
    public Flux<Country> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todos los países");

        return r2dbcRepository.findAll()
                .map(CountryEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando países", error));
    }
}