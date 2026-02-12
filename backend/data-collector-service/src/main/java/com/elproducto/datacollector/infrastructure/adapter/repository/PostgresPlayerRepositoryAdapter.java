package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.Player;
import com.elproducto.datacollector.domain.port.PlayerRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.PlayerEntity;
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
 * Implementa el puerto PlayerRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresPlayerRepositoryAdapter implements PlayerRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresPlayerRepositoryAdapter.class);

    private final PlayerR2dbcRepository playerRepository;

    public PostgresPlayerRepositoryAdapter(PlayerR2dbcRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Mono<List<Player>> saveAll(List<Player> players) {
        logger.info("💾 [POSTGRES] Guardando {} jugadores en PostgreSQL", players.size());

        return Flux.fromIterable(players)
                .flatMap(player -> {
                    // Validar que el jugador tenga los datos mínimos requeridos
                    if (player.apiFootballId() == null || player.apiFootballId() <= 0) {
                        logger.warn("⚠️ [POSTGRES] Jugador '{}' sin ID válido, será omitido", player.name());
                        return Mono.empty();
                    }

                    return savePlayer(player);
                })
                .map(PlayerEntity::toDomain)
                .collectList()
                .doOnSuccess(savedPlayers ->
                    logger.info("💾 [POSTGRES] {} jugadores guardados exitosamente", savedPlayers.size())
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando jugadores", error)
                );
    }

    /**
     * Guarda o actualiza un jugador en la base de datos
     * @param player Jugador del dominio
     * @return Mono con la entidad del jugador guardado
     */
    private Mono<PlayerEntity> savePlayer(Player player) {
        return playerRepository.findByApiFootballId(player.apiFootballId())
            .doOnNext(existing -> {
                existing.markAsNotNew();
                logger.debug("💾 [POSTGRES] Jugador {} ya existe (ID: {}), actualizando...",
                    player.apiFootballId(), existing.getId());
            })
            .flatMap(existing -> {
                // Actualizar jugador existente
                existing.setName(player.name());
                existing.setFirstName(player.firstName());
                existing.setLastName(player.lastName());
                existing.setAge(player.age());
                existing.setBirthDate(player.birthDate());
                existing.setBirthPlace(player.birthPlace());
                existing.setBirthCountry(player.birthCountry());
                existing.setNationality(player.nationality());
                existing.setHeight(player.height());
                existing.setWeight(player.weight());
                existing.setInjured(player.injured());
                existing.setPhoto(player.photo());
                existing.setTeamId(player.teamId());
                existing.setTeamName(player.teamName());
                existing.setUpdatedAt(LocalDateTime.now());
                return playerRepository.save(existing);
            })
            .switchIfEmpty(
                // Crear nuevo jugador
                Mono.defer(() -> {
                    logger.debug("💾 [POSTGRES] Jugador {} es nuevo, creando...", player.apiFootballId());
                    return playerRepository.save(PlayerEntity.fromDomain(player));
                })
            );
    }

    @Override
    public Flux<Player> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todos los jugadores");

        return playerRepository.findAll()
                .map(PlayerEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de jugadores completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando jugadores", error));
    }

    @Override
    public Flux<Player> findByTeamId(Long teamId) {
        logger.info("💾 [POSTGRES] Recuperando jugadores del equipo: {}", teamId);

        return playerRepository.findByTeamId(teamId)
                .map(PlayerEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de jugadores completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando jugadores por equipo", error));
    }

    @Override
    public Flux<Player> findByNationality(String nationality) {
        logger.info("💾 [POSTGRES] Recuperando jugadores de nacionalidad: {}", nationality);

        return playerRepository.findByNationality(nationality)
                .map(PlayerEntity::toDomain)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de jugadores completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando jugadores por nacionalidad", error));
    }

    @Override
    public Mono<Player> findByApiFootballId(Long apiFootballId) {
        logger.info("💾 [POSTGRES] Buscando jugador con API Football ID: {}", apiFootballId);

        return playerRepository.findByApiFootballId(apiFootballId)
                .map(PlayerEntity::toDomain)
                .doOnSuccess(player -> {
                    if (player != null) {
                        logger.info("💾 [POSTGRES] Jugador encontrado: {}", player.name());
                    } else {
                        logger.info("💾 [POSTGRES] Jugador no encontrado");
                    }
                })
                .doOnError(error -> logger.error("❌ [POSTGRES] Error buscando jugador", error));
    }
}