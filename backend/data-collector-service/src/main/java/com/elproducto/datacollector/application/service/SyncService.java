package com.elproducto.datacollector.application.service;

import com.elproducto.datacollector.application.usecase.*;
import com.elproducto.datacollector.domain.model.*;
import com.elproducto.datacollector.domain.port.SyncMetadataRepository;
import com.elproducto.datacollector.infrastructure.config.SyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Servicio de orquestacion de sincronizaciones
 * Coordina la ejecucion de los use cases con tracking de metadata
 */
@Service
public class SyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);

    private final SyncMetadataRepository syncMetadataRepository;
    private final SyncConfiguration syncConfiguration;

    // Use cases
    private final CollectCountriesUseCase collectCountriesUseCase;
    private final CollectLeaguesUseCase collectLeaguesUseCase;
    private final CollectTeamsUseCase collectTeamsUseCase;
    private final CollectPlayersUseCase collectPlayersUseCase;
    private final CollectFixturesUseCase collectFixturesUseCase;
    private final CollectStandingsUseCase collectStandingsUseCase;
    private final CollectFixtureStatisticsUseCase collectFixtureStatisticsUseCase;
    private final CollectFixtureEventsUseCase collectFixtureEventsUseCase;
    private final CollectMatchLineupsUseCase collectMatchLineupsUseCase;

    // Control de sincronizaciones en progreso
    private final Map<String, SyncMetadata> runningSyncs = new ConcurrentHashMap<>();

    public SyncService(
            SyncMetadataRepository syncMetadataRepository,
            SyncConfiguration syncConfiguration,
            CollectCountriesUseCase collectCountriesUseCase,
            CollectLeaguesUseCase collectLeaguesUseCase,
            CollectTeamsUseCase collectTeamsUseCase,
            CollectPlayersUseCase collectPlayersUseCase,
            CollectFixturesUseCase collectFixturesUseCase,
            CollectStandingsUseCase collectStandingsUseCase,
            CollectFixtureStatisticsUseCase collectFixtureStatisticsUseCase,
            CollectFixtureEventsUseCase collectFixtureEventsUseCase,
            CollectMatchLineupsUseCase collectMatchLineupsUseCase) {
        this.syncMetadataRepository = syncMetadataRepository;
        this.syncConfiguration = syncConfiguration;
        this.collectCountriesUseCase = collectCountriesUseCase;
        this.collectLeaguesUseCase = collectLeaguesUseCase;
        this.collectTeamsUseCase = collectTeamsUseCase;
        this.collectPlayersUseCase = collectPlayersUseCase;
        this.collectFixturesUseCase = collectFixturesUseCase;
        this.collectStandingsUseCase = collectStandingsUseCase;
        this.collectFixtureStatisticsUseCase = collectFixtureStatisticsUseCase;
        this.collectFixtureEventsUseCase = collectFixtureEventsUseCase;
        this.collectMatchLineupsUseCase = collectMatchLineupsUseCase;
    }

    /**
     * Sincroniza paises
     */
    public Mono<SyncResult> syncCountries(SyncTrigger trigger) {
        return executeSync(
            EntityType.COUNTRIES,
            null,
            trigger,
            "sync:countries",
            Map.of(),
            metadata -> collectCountriesUseCase.execute()
                .map(countries -> new SyncResult(countries.size(), countries.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza ligas de un pais
     */
    public Mono<SyncResult> syncLeagues(String countryCode, SyncTrigger trigger) {
        return executeSync(
            EntityType.LEAGUES,
            "country:" + countryCode,
            trigger,
            "sync:leagues:" + countryCode,
            Map.of("countryCode", countryCode),
            metadata -> collectLeaguesUseCase.execute(countryCode)
                .map(leagues -> new SyncResult(leagues.size(), leagues.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza equipos de un pais
     */
    public Mono<SyncResult> syncTeams(String country, SyncTrigger trigger) {
        return executeSync(
            EntityType.TEAMS,
            "country:" + country,
            trigger,
            "sync:teams:" + country,
            Map.of("country", country),
            metadata -> collectTeamsUseCase.execute(country)
                .map(teams -> new SyncResult(teams.size(), teams.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza jugadores de un equipo
     */
    public Mono<SyncResult> syncPlayers(Long teamId, Integer season, SyncTrigger trigger) {
        return executeSync(
            EntityType.PLAYERS,
            "team:" + teamId + ":season:" + season,
            trigger,
            "sync:players:" + teamId,
            Map.of("teamId", teamId, "season", season),
            metadata -> collectPlayersUseCase.execute(teamId, season)
                .map(players -> new SyncResult(players.size(), players.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza fixtures de una liga
     */
    public Mono<SyncResult> syncFixtures(Long leagueId, Integer season, SyncTrigger trigger) {
        return executeSync(
            EntityType.FIXTURES,
            "league:" + leagueId + ":season:" + season,
            trigger,
            "sync:fixtures:" + leagueId,
            Map.of("leagueId", leagueId, "season", season),
            metadata -> collectFixturesUseCase.execute(leagueId, season)
                .map(fixtures -> new SyncResult(fixtures.size(), fixtures.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza standings de una liga
     */
    public Mono<SyncResult> syncStandings(Long leagueId, Integer season, SyncTrigger trigger) {
        return executeSync(
            EntityType.STANDINGS,
            "league:" + leagueId + ":season:" + season,
            trigger,
            "sync:standings:" + leagueId,
            Map.of("leagueId", leagueId, "season", season),
            metadata -> collectStandingsUseCase.execute(leagueId, season)
                .map(standings -> new SyncResult(standings.size(), standings.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza estadisticas de un fixture
     */
    public Mono<SyncResult> syncFixtureStatistics(Long fixtureId, SyncTrigger trigger) {
        return executeSync(
            EntityType.FIXTURE_STATISTICS,
            "fixture:" + fixtureId,
            trigger,
            "sync:statistics:" + fixtureId,
            Map.of("fixtureId", fixtureId),
            metadata -> collectFixtureStatisticsUseCase.execute(fixtureId)
                .map(stats -> new SyncResult(stats.size(), stats.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza eventos de un fixture
     */
    public Mono<SyncResult> syncFixtureEvents(Long fixtureId, SyncTrigger trigger) {
        return executeSync(
            EntityType.FIXTURE_EVENTS,
            "fixture:" + fixtureId,
            trigger,
            "sync:events:" + fixtureId,
            Map.of("fixtureId", fixtureId),
            metadata -> collectFixtureEventsUseCase.execute(fixtureId)
                .map(events -> new SyncResult(events.size(), events.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza lineups de un fixture
     */
    public Mono<SyncResult> syncMatchLineups(Long fixtureId, SyncTrigger trigger) {
        return executeSync(
            EntityType.MATCH_LINEUPS,
            "fixture:" + fixtureId,
            trigger,
            "sync:lineups:" + fixtureId,
            Map.of("fixtureId", fixtureId),
            metadata -> collectMatchLineupsUseCase.execute(fixtureId)
                .map(lineups -> new SyncResult(lineups.size(), lineups.size(), 0, 0, 1))
        );
    }

    /**
     * Sincroniza todos los datos de un fixture (stats, events, lineups)
     */
    public Mono<SyncResult> syncFullFixtureData(Long fixtureId, SyncTrigger trigger) {
        logger.info("[SYNC] Sincronizando datos completos del fixture: {}", fixtureId);

        return Mono.zip(
            syncFixtureStatistics(fixtureId, trigger).onErrorResume(e -> {
                logger.warn("[SYNC] Error sincronizando stats de fixture {}: {}", fixtureId, e.getMessage());
                return Mono.just(SyncResult.empty());
            }),
            syncFixtureEvents(fixtureId, trigger).onErrorResume(e -> {
                logger.warn("[SYNC] Error sincronizando events de fixture {}: {}", fixtureId, e.getMessage());
                return Mono.just(SyncResult.empty());
            }),
            syncMatchLineups(fixtureId, trigger).onErrorResume(e -> {
                logger.warn("[SYNC] Error sincronizando lineups de fixture {}: {}", fixtureId, e.getMessage());
                return Mono.just(SyncResult.empty());
            })
        ).map(tuple -> {
            SyncResult stats = tuple.getT1();
            SyncResult events = tuple.getT2();
            SyncResult lineups = tuple.getT3();
            return new SyncResult(
                stats.processed() + events.processed() + lineups.processed(),
                stats.created() + events.created() + lineups.created(),
                stats.updated() + events.updated() + lineups.updated(),
                stats.deleted() + events.deleted() + lineups.deleted(),
                stats.apiCalls() + events.apiCalls() + lineups.apiCalls()
            );
        });
    }

    /**
     * Ejecuta una sincronizacion con tracking de metadata
     */
    private Mono<SyncResult> executeSync(
            EntityType entityType,
            String scope,
            SyncTrigger trigger,
            String triggerEvent,
            Map<String, Object> parameters,
            java.util.function.Function<SyncMetadata, Mono<SyncResult>> syncAction) {

        String syncKey = entityType.name() + ":" + (scope != null ? scope : "global");

        // Verificar si ya hay una sync en progreso
        return syncMetadataRepository.isRunning(entityType, scope)
            .flatMap(isRunning -> {
                if (isRunning) {
                    logger.warn("[SYNC] Sincronizacion ya en progreso para: {}", syncKey);
                    return Mono.just(SyncResult.skipped());
                }

                // Obtener configuracion
                SyncConfiguration.EntitySyncConfig config = syncConfiguration.getConfig(entityType);
                SyncStrategy strategy = config != null ? config.getStrategy() : SyncStrategy.UPSERT;

                // Crear metadata
                SyncMetadata metadata = SyncMetadata.create(
                    entityType,
                    scope,
                    strategy,
                    trigger,
                    triggerEvent,
                    parameters
                );

                // Guardar metadata inicial y ejecutar sync
                return syncMetadataRepository.save(metadata.start())
                    .flatMap(savedMetadata -> {
                        runningSyncs.put(syncKey, savedMetadata);
                        logger.info("[SYNC] Iniciando sincronizacion: {} (id={})", syncKey, savedMetadata.id());

                        return syncAction.apply(savedMetadata)
                            .flatMap(result -> {
                                // Actualizar metadata con resultado exitoso
                                SyncMetadata completed = savedMetadata.complete(
                                    result.processed(),
                                    result.created(),
                                    result.updated(),
                                    result.deleted(),
                                    0,
                                    result.apiCalls()
                                );
                                return syncMetadataRepository.save(completed)
                                    .doOnSuccess(s -> {
                                        runningSyncs.remove(syncKey);
                                        logger.info("[SYNC] Sincronizacion completada: {} - {} registros procesados en {}s",
                                            syncKey, result.processed(), completed.getDuration().toSeconds());
                                    })
                                    .thenReturn(result);
                            })
                            .onErrorResume(error -> {
                                // Actualizar metadata con error
                                SyncMetadata failed = savedMetadata.fail(
                                    error.getMessage(),
                                    Map.of("exception", error.getClass().getSimpleName())
                                );
                                return syncMetadataRepository.save(failed)
                                    .doOnSuccess(s -> {
                                        runningSyncs.remove(syncKey);
                                        logger.error("[SYNC] Sincronizacion fallida: {} - {}", syncKey, error.getMessage());
                                    })
                                    .then(Mono.error(error));
                            });
                    });
            });
    }

    /**
     * Obtiene la ultima sincronizacion exitosa de una entidad
     */
    public Mono<SyncMetadata> getLastSync(EntityType entityType) {
        return syncMetadataRepository.findLastSuccessful(entityType);
    }

    /**
     * Obtiene sincronizaciones en ejecucion
     */
    public Flux<SyncMetadata> getRunningSyncs() {
        return syncMetadataRepository.findRunning();
    }

    /**
     * Obtiene historial de sincronizaciones
     */
    public Flux<SyncMetadata> getSyncHistory(EntityType entityType, int limit) {
        return syncMetadataRepository.findByEntityType(entityType, limit);
    }

    /**
     * Obtiene estadisticas de sincronizacion
     */
    public Mono<SyncMetadataRepository.SyncStats> getSyncStats(EntityType entityType) {
        return syncMetadataRepository.getStats(entityType, LocalDateTime.now().minusDays(30));
    }

    /**
     * Cancela sincronizaciones antiguas
     */
    public Mono<Integer> cancelStaleSyncs() {
        LocalDateTime staleThreshold = LocalDateTime.now()
            .minus(syncConfiguration.getStaleTimeout());
        return syncMetadataRepository.cancelStale(staleThreshold);
    }

    /**
     * Resultado de una sincronizacion
     */
    public record SyncResult(
        int processed,
        int created,
        int updated,
        int deleted,
        int apiCalls
    ) {
        public static SyncResult empty() {
            return new SyncResult(0, 0, 0, 0, 0);
        }

        public static SyncResult skipped() {
            return new SyncResult(-1, 0, 0, 0, 0);
        }

        public boolean wasSkipped() {
            return processed < 0;
        }
    }
}