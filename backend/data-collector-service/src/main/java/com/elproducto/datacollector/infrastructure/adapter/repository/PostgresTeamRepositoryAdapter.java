package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.Team;
import com.elproducto.datacollector.domain.model.Venue;
import com.elproducto.datacollector.domain.port.TeamRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.TeamEntity;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.VenueEntity;
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
 * Implementa el puerto TeamRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresTeamRepositoryAdapter implements TeamRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresTeamRepositoryAdapter.class);

    private final TeamR2dbcRepository teamRepository;
    private final VenueR2dbcRepository venueRepository;

    public PostgresTeamRepositoryAdapter(TeamR2dbcRepository teamRepository,
                                          VenueR2dbcRepository venueRepository) {
        this.teamRepository = teamRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public Mono<List<Team>> saveAll(List<Team> teams) {
        logger.info("💾 [POSTGRES] Guardando {} equipos en PostgreSQL", teams.size());

        return Flux.fromIterable(teams)
                .flatMap(team -> {
                    // Validar que el equipo tenga los datos mínimos requeridos
                    if (team.apiFootballId() == null || team.apiFootballId() <= 0) {
                        logger.warn("⚠️ [POSTGRES] Equipo '{}' sin ID válido, será omitido", team.name());
                        return Mono.empty();
                    }

                    // Primero guardar el venue (si existe)
                    Mono<Long> venueIdMono = team.venue() != null
                        ? saveVenue(team.venue())
                        : Mono.justOrEmpty(null);

                    // Luego guardar el equipo con el venueId
                    return venueIdMono.flatMap(venueId -> saveTeam(team, venueId));
                })
                .map(teamEntity -> loadTeamWithVenue(teamEntity))
                .flatMap(mono -> mono)
                .collectList()
                .doOnSuccess(savedTeams ->
                    logger.info("💾 [POSTGRES] {} equipos guardados exitosamente", savedTeams.size())
                )
                .doOnError(error ->
                    logger.error("❌ [POSTGRES] Error guardando equipos", error)
                );
    }

    /**
     * Guarda o actualiza un venue en la base de datos
     * @param venue Venue del dominio
     * @return Mono con el ID del venue guardado
     */
    private Mono<Long> saveVenue(Venue venue) {
        return venueRepository.findByApiFootballId(venue.apiFootballId())
            .doOnNext(existing -> {
                existing.markAsNotNew();
                logger.debug("💾 [POSTGRES] Venue {} ya existe (ID: {}), actualizando...",
                    venue.apiFootballId(), existing.getId());
            })
            .flatMap(existing -> {
                // Actualizar venue existente
                existing.setName(venue.name());
                existing.setAddress(venue.address());
                existing.setCity(venue.city());
                existing.setCapacity(venue.capacity());
                existing.setSurface(venue.surface());
                existing.setImage(venue.image());
                existing.setUpdatedAt(LocalDateTime.now());
                return venueRepository.save(existing);
            })
            .switchIfEmpty(
                // Crear nuevo venue
                Mono.defer(() -> {
                    logger.debug("💾 [POSTGRES] Venue {} es nuevo, creando...", venue.apiFootballId());
                    return venueRepository.save(VenueEntity.fromDomain(venue));
                })
            )
            .map(VenueEntity::getId);
    }

    /**
     * Guarda o actualiza un equipo en la base de datos
     * @param team Equipo del dominio
     * @param venueId ID del venue (puede ser null)
     * @return Mono con la entidad del equipo guardado
     */
    private Mono<TeamEntity> saveTeam(Team team, Long venueId) {
        return teamRepository.findByApiFootballId(team.apiFootballId())
            .doOnNext(existing -> {
                existing.markAsNotNew();
                logger.debug("💾 [POSTGRES] Equipo {} ya existe (ID: {}), actualizando...",
                    team.apiFootballId(), existing.getId());
            })
            .flatMap(existing -> {
                // Actualizar equipo existente
                existing.setName(team.name());
                existing.setCode(team.code());
                existing.setCountry(team.country());
                existing.setFounded(team.founded());
                existing.setNational(team.national());
                existing.setLogo(team.logo());
                existing.setVenueId(venueId);
                existing.setUpdatedAt(LocalDateTime.now());
                return teamRepository.save(existing);
            })
            .switchIfEmpty(
                // Crear nuevo equipo
                Mono.defer(() -> {
                    logger.debug("💾 [POSTGRES] Equipo {} es nuevo, creando...", team.apiFootballId());
                    return teamRepository.save(TeamEntity.fromDomain(team, venueId));
                })
            );
    }

    /**
     * Carga un equipo con su venue asociado
     * @param teamEntity Entidad del equipo
     * @return Mono con el equipo del dominio
     */
    private Mono<Team> loadTeamWithVenue(TeamEntity teamEntity) {
        if (teamEntity.getVenueId() == null) {
            return Mono.just(teamEntity.toDomain(null));
        }

        return venueRepository.findById(teamEntity.getVenueId())
            .map(VenueEntity::toDomain)
            .map(venue -> teamEntity.toDomain(venue))
            .switchIfEmpty(Mono.just(teamEntity.toDomain(null)));
    }

    @Override
    public Flux<Team> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todos los equipos");

        return teamRepository.findAll()
                .flatMap(this::loadTeamWithVenue)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de equipos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando equipos", error));
    }

    @Override
    public Flux<Team> findByCountry(String country) {
        logger.info("💾 [POSTGRES] Recuperando equipos del país: {}", country);

        return teamRepository.findByCountry(country)
                .flatMap(this::loadTeamWithVenue)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de equipos completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando equipos por país", error));
    }
}