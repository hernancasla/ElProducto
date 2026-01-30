package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.port.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Repositorio en memoria (mock) para países
 * Implementación temporal para desarrollo y testing
 * NOTA: PostgresCountryRepositoryAdapter es @Primary, por lo que esta implementación
 * solo se usará si PostgreSQL no está disponible
 */
@Repository
public class InMemoryCountryRepository implements CountryRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCountryRepository.class);

    // Almacenamiento en memoria thread-safe
    private final ConcurrentMap<String, Country> storage = new ConcurrentHashMap<>();

    @Override
    public Mono<List<Country>> saveAll(List<Country> countries) {
        logger.info("💾 [MOCK REPOSITORY] Guardando {} países en memoria", countries.size());

        return Mono.fromCallable(() -> {
            countries.forEach(country -> {
                storage.put(country.name(), country);
                logger.debug("💾 [MOCK] País guardado: {}", country);
            });

            logger.info("💾 [MOCK REPOSITORY] Total de países en almacenamiento: {}", storage.size());
            return new ArrayList<>(storage.values());
        });
    }

    @Override
    public Flux<Country> findAll() {
        logger.info("💾 [MOCK REPOSITORY] Recuperando todos los países. Total: {}", storage.size());
        return Flux.fromIterable(storage.values());
    }

    /**
     * Método auxiliar para limpiar el almacenamiento (útil para testing)
     */
    public void clear() {
        storage.clear();
        logger.info("💾 [MOCK REPOSITORY] Almacenamiento limpiado");
    }

    /**
     * Método auxiliar para obtener el tamaño actual del almacenamiento
     */
    public int size() {
        return storage.size();
    }
}