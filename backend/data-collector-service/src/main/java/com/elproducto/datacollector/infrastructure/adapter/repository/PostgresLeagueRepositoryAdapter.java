package com.elproducto.datacollector.infrastructure.adapter.repository;

import com.elproducto.datacollector.domain.model.League;
import com.elproducto.datacollector.domain.model.Season;
import com.elproducto.datacollector.domain.model.SeasonCoverage;
import com.elproducto.datacollector.domain.port.LeagueRepository;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.LeagueEntity;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.SeasonCoverageEntity;
import com.elproducto.datacollector.infrastructure.adapter.repository.entity.SeasonEntity;
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
 * Implementa el puerto LeagueRepository con persistencia reactiva en PostgreSQL
 */
@Repository
@Primary
public class PostgresLeagueRepositoryAdapter implements LeagueRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostgresLeagueRepositoryAdapter.class);

    private final LeagueR2dbcRepository leagueRepository;
    private final SeasonR2dbcRepository seasonRepository;
    private final SeasonCoverageR2dbcRepository coverageRepository;
    private final CountryR2dbcRepository countryRepository;

    public PostgresLeagueRepositoryAdapter(
            LeagueR2dbcRepository leagueRepository,
            SeasonR2dbcRepository seasonRepository,
            SeasonCoverageR2dbcRepository coverageRepository,
            CountryR2dbcRepository countryRepository) {
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.coverageRepository = coverageRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public Mono<List<League>> saveAll(List<League> leagues) {
        logger.info("💾 [POSTGRES] Guardando {} ligas en PostgreSQL", leagues.size());

        return Flux.fromIterable(leagues)
                .flatMap(this::saveLeagueWithSeasons)
                .collectList()
                .doOnSuccess(savedLeagues ->
                        logger.info("💾 [POSTGRES] {} ligas guardadas exitosamente", savedLeagues.size())
                )
                .doOnError(error ->
                        logger.error("❌ [POSTGRES] Error guardando ligas", error)
                );
    }

    /**
     * Guarda una liga con todas sus temporadas y coberturas
     */
    private Mono<League> saveLeagueWithSeasons(League league) {
        logger.debug("💾 [POSTGRES] Procesando liga {} (API ID: {})", league.name(), league.apiFootballId());

        // Buscar si la liga ya existe por API Football ID
        return leagueRepository.findByApiFootballId(league.apiFootballId())
                .doOnNext(existing -> {
                    existing.markAsNotNew();
                    logger.debug("💾 [POSTGRES] Liga {} ya existe (ID: {}), actualizando...",
                            league.apiFootballId(), existing.getId());
                })
                .flatMap(existing -> {
                    // Si existe, actualizar
                    existing.setName(league.name());
                    existing.setType(league.type());
                    existing.setLogo(league.logo());
                    existing.setCountryCode(league.countryCode());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return leagueRepository.save(existing);
                })
                .switchIfEmpty(
                        // Si no existe, crear nueva
                        Mono.defer(() -> {
                            logger.debug("💾 [POSTGRES] Liga {} es nueva, creando...", league.apiFootballId());
                            return leagueRepository.save(LeagueEntity.fromDomain(league));
                        })
                )
                .flatMap(savedLeague ->
                        // Guardar las temporadas de la liga
                        saveSeasons(savedLeague.getId(), league.seasons())
                                .collectList()
                                .flatMap(seasons ->
                                        // Obtener el nombre del país y construir el objeto League completo
                                        countryRepository.findByCode(league.countryCode())
                                                .map(country -> savedLeague.toDomain(country.getName(), seasons))
                                                .defaultIfEmpty(savedLeague.toDomain(league.countryName(), seasons))
                                )
                );
    }

    /**
     * Guarda las temporadas de una liga
     */
    private Flux<Season> saveSeasons(Long leagueId, List<Season> seasons) {
        return Flux.fromIterable(seasons)
                .flatMap(season -> saveSeasonWithCoverage(leagueId, season));
    }

    /**
     * Guarda una temporada con su cobertura
     */
    private Mono<Season> saveSeasonWithCoverage(Long leagueId, Season season) {
        logger.debug("💾 [POSTGRES] Procesando temporada {} de liga ID {}", season.year(), leagueId);

        // Buscar si la temporada ya existe
        return seasonRepository.findByLeagueIdAndYear(leagueId, season.year())
                .doOnNext(existing -> {
                    existing.markAsNotNew();
                    logger.debug("💾 [POSTGRES] Temporada {}/{} ya existe (ID: {}), actualizando...",
                            leagueId, season.year(), existing.getId());
                })
                .flatMap(existing -> {
                    // Si existe, actualizar
                    existing.setStartDate(season.startDate());
                    existing.setEndDate(season.endDate());
                    existing.setCurrent(season.current());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return seasonRepository.save(existing);
                })
                .switchIfEmpty(
                        // Si no existe, crear nueva
                        Mono.defer(() -> {
                            logger.debug("💾 [POSTGRES] Temporada {}/{} es nueva, creando...", leagueId, season.year());
                            return seasonRepository.save(SeasonEntity.fromDomain(leagueId, season));
                        })
                )
                .flatMap(savedSeason ->
                        // Guardar la cobertura de la temporada
                        saveCoverage(savedSeason.getId(), season.coverage())
                                .map(coverage -> savedSeason.toDomain(coverage))
                );
    }

    /**
     * Guarda la cobertura de una temporada
     */
    private Mono<SeasonCoverage> saveCoverage(Long seasonId, SeasonCoverage coverage) {
        logger.debug("💾 [POSTGRES] Guardando cobertura para temporada ID {}", seasonId);

        // Buscar si la cobertura ya existe
        return coverageRepository.findBySeasonId(seasonId)
                .doOnNext(existing -> {
                    existing.markAsNotNew();
                    logger.debug("💾 [POSTGRES] Cobertura de temporada {} ya existe, actualizando...", seasonId);
                })
                .flatMap(existing -> {
                    // Si existe, actualizar
                    existing.setFixturesEvents(coverage.fixturesEvents());
                    existing.setFixturesLineups(coverage.fixturesLineups());
                    existing.setFixturesStatistics(coverage.fixturesStatistics());
                    existing.setPlayersStatistics(coverage.playersStatistics());
                    existing.setStandings(coverage.standings());
                    existing.setPlayers(coverage.players());
                    existing.setTopScorers(coverage.topScorers());
                    existing.setTopAssists(coverage.topAssists());
                    existing.setTopCards(coverage.topCards());
                    existing.setInjuries(coverage.injuries());
                    existing.setPredictions(coverage.predictions());
                    existing.setOdds(coverage.odds());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return coverageRepository.save(existing);
                })
                .switchIfEmpty(
                        // Si no existe, crear nueva
                        Mono.defer(() -> {
                            logger.debug("💾 [POSTGRES] Cobertura de temporada {} es nueva, creando...", seasonId);
                            return coverageRepository.save(SeasonCoverageEntity.fromDomain(seasonId, coverage));
                        })
                )
                .map(SeasonCoverageEntity::toDomain);
    }

    @Override
    public Flux<League> findByCountryCode(String countryCode) {
        logger.info("💾 [POSTGRES] Recuperando ligas del país {}", countryCode);

        return leagueRepository.findByCountryCode(countryCode)
                .flatMap(this::loadLeagueWithSeasons)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de ligas completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando ligas", error));
    }

    @Override
    public Flux<League> findAll() {
        logger.info("💾 [POSTGRES] Recuperando todas las ligas");

        return leagueRepository.findAll()
                .flatMap(this::loadLeagueWithSeasons)
                .doOnComplete(() -> logger.info("💾 [POSTGRES] Recuperación de ligas completada"))
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando ligas", error));
    }

    @Override
    public Mono<League> findByApiFootballId(Long apiFootballId) {
        logger.info("💾 [POSTGRES] Recuperando liga con API ID {}", apiFootballId);

        return leagueRepository.findByApiFootballId(apiFootballId)
                .flatMap(this::loadLeagueWithSeasons)
                .doOnSuccess(league -> {
                    if (league != null) {
                        logger.info("💾 [POSTGRES] Liga {} recuperada exitosamente", apiFootballId);
                    }
                })
                .doOnError(error -> logger.error("❌ [POSTGRES] Error recuperando liga", error));
    }

    /**
     * Carga una liga con todas sus temporadas y coberturas
     */
    private Mono<League> loadLeagueWithSeasons(LeagueEntity leagueEntity) {
        // Cargar el nombre del país
        Mono<String> countryNameMono = countryRepository.findByCode(leagueEntity.getCountryCode())
                .map(country -> country.getName())
                .defaultIfEmpty("Unknown");

        // Cargar las temporadas con sus coberturas
        Flux<Season> seasonsFlux = seasonRepository.findByLeagueId(leagueEntity.getId())
                .flatMap(this::loadSeasonWithCoverage);

        return Mono.zip(countryNameMono, seasonsFlux.collectList())
                .map(tuple -> {
                    String countryName = tuple.getT1();
                    List<Season> seasons = tuple.getT2();
                    return leagueEntity.toDomain(countryName, seasons);
                });
    }

    /**
     * Carga una temporada con su cobertura
     */
    private Mono<Season> loadSeasonWithCoverage(SeasonEntity seasonEntity) {
        return coverageRepository.findBySeasonId(seasonEntity.getId())
                .map(SeasonCoverageEntity::toDomain)
                .defaultIfEmpty(new SeasonCoverage(false, false, false, false, false, false, false, false, false, false, false, false))
                .map(coverage -> seasonEntity.toDomain(coverage));
    }
}