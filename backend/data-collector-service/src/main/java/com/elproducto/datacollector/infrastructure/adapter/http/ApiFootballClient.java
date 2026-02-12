package com.elproducto.datacollector.infrastructure.adapter.http;

import com.elproducto.datacollector.application.service.ApiQuotaService;
import com.elproducto.datacollector.domain.exception.ApiQuotaExceededException;
import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.model.Fixture;
import com.elproducto.datacollector.domain.model.FixtureEvent;
import com.elproducto.datacollector.domain.model.FixtureStatistics;
import com.elproducto.datacollector.domain.model.League;
import com.elproducto.datacollector.domain.model.MatchLineup;
import com.elproducto.datacollector.domain.model.Player;
import com.elproducto.datacollector.domain.model.Season;
import com.elproducto.datacollector.domain.model.SeasonCoverage;
import com.elproducto.datacollector.domain.model.Standing;
import com.elproducto.datacollector.domain.model.Team;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.infrastructure.adapter.http.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de infraestructura para consumir API-Football
 * Implementa el puerto FootballApiClient usando WebClient
 */
@Component
public class ApiFootballClient implements FootballApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiFootballClient.class);

    private final WebClient webClient;
    private final ApiQuotaService quotaService;

    public ApiFootballClient(
            @Value("${api-football.base-url}") String baseUrl,
            @Value("${api-football.api-key}") String apiKey,
            @Value("${api-football.timeout}") int timeout,
            ApiQuotaService quotaService) {

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-apisports-key", apiKey)
                .build();
        this.quotaService = quotaService;

        logger.info("ApiFootballClient initialized with base URL: {} (quota control enabled)", baseUrl);
    }

    /**
     * Verifica la quota antes de hacer una llamada a la API
     * @param apiCall El Mono que representa la llamada a la API
     * @param endpoint Nombre del endpoint para logging
     * @return Mono con el resultado de la llamada o error si se excedio la quota
     */
    private <T> Mono<T> withQuotaCheck(Mono<T> apiCall, String endpoint) {
        return quotaService.canMakeCall()
                .flatMap(canMake -> {
                    if (!canMake) {
                        var status = quotaService.getStatus();
                        logger.warn("[QUOTA] Llamada bloqueada a {} - Limite alcanzado: {}/{}",
                                endpoint, status.callsToday(), status.dailyLimit());
                        return Mono.error(new ApiQuotaExceededException(status.callsToday(), status.dailyLimit()));
                    }
                    return apiCall.doOnSuccess(result -> {
                        quotaService.recordCall();
                        var status = quotaService.getStatus();
                        logger.debug("[QUOTA] Llamada exitosa a {} - Contador: {}/{} ({}% usado)",
                                endpoint, status.callsToday(), status.dailyLimit(), status.percentUsed());
                    });
                });
    }

    @Override
    public Mono<List<Country>> fetchCountries() {
        logger.info("Fetching countries from API-Football endpoint: /countries");

        Mono<List<Country>> apiCall = webClient.get()
                .uri("/countries")
                .retrieve()
                .bodyToMono(ApiFootballResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("API Response received - Total countries: {}",
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("Full API Response: {}", response);
                })
                .map(response -> {
                    if (response == null || response.response() == null) {
                        logger.warn("Empty response received from API");
                        return List.<Country>of();
                    }

                    // Convertir DTOs a modelos de dominio
                    List<Country> countries = response.response().stream()
                            .map(dto -> new Country(
                                    dto.name(),
                                    dto.code(),
                                    dto.flag()
                            ))
                            .collect(Collectors.toList());

                    logger.info("Converted {} countries to domain models", countries.size());
                    return countries;
                })
                .doOnError(e -> logger.error("Error fetching countries from API-Football", e))
                .onErrorMap(e -> !(e instanceof ApiQuotaExceededException),
                        e -> new RuntimeException("Failed to fetch countries from external API", e));

        return withQuotaCheck(apiCall, "/countries");
    }

    @Override
    public Mono<List<League>> fetchLeaguesByCountry(String countryCode) {
        logger.info("Fetching leagues from API-Football endpoint: /leagues?code={}", countryCode);

        Mono<List<League>> apiCall = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/leagues")
                        .queryParam("code", countryCode)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballLeaguesResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("API Response received - Results: {}, Total leagues: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("Full API Response: {}", response);

                    if (response.errors() != null) {
                        logger.debug("API errors field: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null) {
                        logger.warn("Empty response received from API");
                        return List.<League>of();
                    }

                    List<League> leagues = response.response().stream()
                            .filter(dto -> {
                                if (dto.league() == null) {
                                    logger.warn("Skipping league with null league data");
                                    return false;
                                }
                                if (dto.league().id() == null || dto.league().id() <= 0) {
                                    logger.warn("Skipping league with invalid ID: {}", dto.league());
                                    return false;
                                }
                                return true;
                            })
                            .map(dto -> {
                                try {
                                    return this.convertToLeague(dto);
                                } catch (Exception e) {
                                    logger.error("Error converting league: {}", dto, e);
                                    throw e;
                                }
                            })
                            .collect(Collectors.toList());

                    logger.info("Converted {} leagues to domain models", leagues.size());
                    return leagues;
                })
                .doOnError(e -> logger.error("Error fetching leagues from API-Football for country {}", countryCode, e))
                .onErrorMap(e -> !(e instanceof ApiQuotaExceededException),
                        e -> new RuntimeException("Failed to fetch leagues from external API: " + e.getMessage(), e));

        return withQuotaCheck(apiCall, "/leagues");
    }

    /**
     * Convierte un LeagueResponseDto a un modelo de dominio League
     */
    private League convertToLeague(LeagueResponseDto dto) {
        LeagueDto leagueDto = dto.league();
        CountryDto countryDto = dto.country();

        // Convertir temporadas
        List<Season> seasons = dto.seasons() != null
                ? dto.seasons().stream()
                    .filter(seasonDto -> seasonDto.year() != null)
                    .map(this::convertToSeason)
                    .collect(Collectors.toList())
                : List.of();

        return new League(
            leagueDto.id(),
            leagueDto.name(),
            leagueDto.type(),
            leagueDto.logo(),
            countryDto != null ? countryDto.code() : null,
            countryDto != null ? countryDto.name() : null,
            seasons
        );
    }

    /**
     * Convierte un SeasonDto a un modelo de dominio Season
     */
    private Season convertToSeason(SeasonDto dto) {
        // Parsear fechas con fallback basado en el año de la temporada
        LocalDate startDate = parseDate(dto.start(), dto.year(), true);
        LocalDate endDate = parseDate(dto.end(), dto.year(), false);

        // Si el parseo falló y las fechas son iguales o invertidas, ajustarlas
        if (endDate.isBefore(startDate) || endDate.equals(startDate)) {
            logger.warn("⚠️ Fechas inválidas para temporada {}: start={}, end={}. Ajustando...",
                dto.year(), dto.start(), dto.end());
            // Usar fechas predeterminadas basadas en el año
            startDate = LocalDate.of(dto.year(), 1, 1);
            endDate = LocalDate.of(dto.year(), 12, 31);
        }

        SeasonCoverage coverage = convertToCoverage(dto.coverage());

        return new Season(
            dto.year(),
            startDate,
            endDate,
            dto.current(),
            coverage
        );
    }

    /**
     * Convierte un CoverageDto a un modelo de dominio SeasonCoverage
     */
    private SeasonCoverage convertToCoverage(CoverageDto dto) {
        if (dto == null) {
            return new SeasonCoverage(false, false, false, false, false, false, false, false, false, false, false, false);
        }

        FixturesCoverageDto fixtures = dto.fixtures();
        return new SeasonCoverage(
            fixtures != null && fixtures.events(),
            fixtures != null && fixtures.lineups(),
            fixtures != null && fixtures.statisticsFixtures(),
            fixtures != null && fixtures.statisticsPlayers(),
            dto.standings(),
            dto.players(),
            dto.topScorers(),
            dto.topAssists(),
            dto.topCards(),
            dto.injuries(),
            dto.predictions(),
            dto.odds()
        );
    }

    /**
     * Parsea una fecha en formato String (yyyy-MM-dd) a LocalDate
     * @param dateStr String de fecha en formato yyyy-MM-dd
     * @param year Año de la temporada para usar como fallback
     * @param isStartDate true si es fecha de inicio, false si es fecha de fin
     * @return LocalDate parseada o fecha predeterminada
     */
    private LocalDate parseDate(String dateStr, Integer year, boolean isStartDate) {
        if (dateStr == null || dateStr.isBlank()) {
            logger.warn("⚠️ Empty date string for year {}, using default {} date",
                year, isStartDate ? "start" : "end");
            return isStartDate
                ? LocalDate.of(year, 1, 1)   // Inicio de año
                : LocalDate.of(year, 12, 31); // Fin de año
        }

        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            logger.warn("⚠️ Failed to parse date '{}' for year {}, using default {} date",
                dateStr, year, isStartDate ? "start" : "end");
            return isStartDate
                ? LocalDate.of(year, 1, 1)   // Inicio de año
                : LocalDate.of(year, 12, 31); // Fin de año
        }
    }

    @Override
    public Mono<List<Team>> fetchTeamsByCountry(String country) {
        logger.info("🌐 Fetching teams from API-Football endpoint: /teams?country={}", country);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/teams")
                        .queryParam("country", country)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballTeamsResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}, Total teams: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.errors() != null && !response.errors().isEmpty()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null) {
                        logger.warn("⚠️ Empty response received from API");
                        return List.<Team>of();
                    }

                    // Convertir DTOs a modelos de dominio
                    List<Team> teams = response.response().stream()
                            .filter(dto -> {
                                if (dto.team() == null) {
                                    logger.warn("⚠️ Skipping team with null team data");
                                    return false;
                                }
                                if (dto.team().id() == null || dto.team().id() <= 0) {
                                    logger.warn("⚠️ Skipping team with invalid ID: {}", dto.team());
                                    return false;
                                }
                                return true;
                            })
                            .map(TeamResponseDto::toDomain)
                            .collect(Collectors.toList());

                    logger.info("✅ Converted {} teams to domain models", teams.size());
                    return teams;
                })
                .doOnError(e -> logger.error("❌ Error fetching teams from API-Football for country {}", country, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch teams from external API: " + e.getMessage(), e);
                });
    }

    @Override
    public Mono<List<Player>> fetchPlayersByTeam(Long teamId, Integer season) {
        logger.info("🌐 Fetching players from API-Football endpoint: /players?team={}&season={}", teamId, season);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/players")
                        .queryParam("team", teamId)
                        .queryParam("season", season)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballPlayersResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}, Total players: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.hasErrors()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .expand(response -> {
                    // Manejar paginación: si hay más páginas, obtener la siguiente
                    if (response.hasMorePages()) {
                        int nextPage = response.paging().current() + 1;
                        logger.info("📄 Fetching page {} of {} for team {}", nextPage, response.paging().total(), teamId);
                        return webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/players")
                                        .queryParam("team", teamId)
                                        .queryParam("season", season)
                                        .queryParam("page", nextPage)
                                        .build()
                                )
                                .retrieve()
                                .bodyToMono(ApiFootballPlayersResponse.class)
                                .timeout(Duration.ofSeconds(30));
                    }
                    return Mono.empty();
                })
                .flatMapIterable(response -> {
                    if (response == null || response.response() == null) {
                        return List.<Player>of();
                    }
                    return response.response().stream()
                            .filter(dto -> {
                                if (dto.player() == null) {
                                    logger.warn("⚠️ Skipping player with null player data");
                                    return false;
                                }
                                if (dto.player().id() == null || dto.player().id() <= 0) {
                                    logger.warn("⚠️ Skipping player with invalid ID: {}", dto.player());
                                    return false;
                                }
                                return true;
                            })
                            .map(PlayerResponseDto::toDomain)
                            .toList();
                })
                .collectList()
                .doOnNext(players -> logger.info("✅ Converted {} players to domain models for team {}", players.size(), teamId))
                .doOnError(e -> logger.error("❌ Error fetching players from API-Football for team {}", teamId, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch players from external API: " + e.getMessage(), e);
                });
    }

    @Override
    public Mono<List<Fixture>> fetchFixturesByLeague(Long leagueId, Integer season) {
        logger.info("🌐 Fetching fixtures from API-Football endpoint: /fixtures?league={}&season={}", leagueId, season);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("league", leagueId)
                        .queryParam("season", season)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballFixturesResponse.class)
                .timeout(Duration.ofSeconds(60)) // Más tiempo para fixtures (pueden ser muchos)
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}, Total fixtures: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.hasErrors()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null) {
                        logger.warn("⚠️ Empty response received from API");
                        return List.<Fixture>of();
                    }

                    // Convertir DTOs a modelos de dominio
                    List<Fixture> fixtures = response.response().stream()
                            .filter(dto -> {
                                if (dto.fixture() == null) {
                                    logger.warn("⚠️ Skipping fixture with null fixture data");
                                    return false;
                                }
                                if (dto.fixture().id() == null || dto.fixture().id() <= 0) {
                                    logger.warn("⚠️ Skipping fixture with invalid ID: {}", dto.fixture());
                                    return false;
                                }
                                return true;
                            })
                            .map(FixtureResponseDto::toDomain)
                            .toList();

                    logger.info("✅ Converted {} fixtures to domain models for league {}", fixtures.size(), leagueId);
                    return fixtures;
                })
                .doOnError(e -> logger.error("❌ Error fetching fixtures from API-Football for league {}", leagueId, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch fixtures from external API: " + e.getMessage(), e);
                });
    }

    @Override
    public Mono<List<Standing>> fetchStandingsByLeague(Long leagueId, Integer season) {
        logger.info("🌐 Fetching standings from API-Football endpoint: /standings?league={}&season={}", leagueId, season);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/standings")
                        .queryParam("league", leagueId)
                        .queryParam("season", season)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballStandingsResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}",
                            response.results());
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.hasErrors()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null || response.response().isEmpty()) {
                        logger.warn("⚠️ Empty response received from API");
                        return List.<Standing>of();
                    }

                    // La respuesta tiene una estructura anidada: response[0].league.standings[][]
                    StandingsLeagueResponseDto leagueResponse = response.response().get(0);
                    if (leagueResponse.league() == null || leagueResponse.league().standings() == null) {
                        logger.warn("⚠️ No standings data in response");
                        return List.<Standing>of();
                    }

                    StandingsLeagueDto league = leagueResponse.league();

                    // Aplanar el array de arrays (grupos) a una lista única
                    List<Standing> standings = league.standings().stream()
                            .flatMap(group -> group.stream())
                            .filter(entry -> {
                                if (entry.team() == null || entry.team().id() == null) {
                                    logger.warn("⚠️ Skipping standing with invalid team data");
                                    return false;
                                }
                                return true;
                            })
                            .map(entry -> entry.toDomain(
                                    league.id(),
                                    league.name(),
                                    league.country(),
                                    league.logo(),
                                    league.season()
                            ))
                            .toList();

                    logger.info("✅ Converted {} standings to domain models for league {}", standings.size(), leagueId);
                    return standings;
                })
                .doOnError(e -> logger.error("❌ Error fetching standings from API-Football for league {}", leagueId, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch standings from external API: " + e.getMessage(), e);
                });
    }

    @Override
    public Mono<List<FixtureStatistics>> fetchStatisticsByFixture(Long fixtureId) {
        logger.info("🌐 Fetching statistics from API-Football endpoint: /fixtures/statistics?fixture={}", fixtureId);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures/statistics")
                        .queryParam("fixture", fixtureId)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballStatisticsResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}, Total statistics: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.hasErrors()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null || response.response().isEmpty()) {
                        logger.warn("⚠️ Empty response received from API");
                        return List.<FixtureStatistics>of();
                    }

                    // Convertir DTOs a modelos de dominio
                    List<FixtureStatistics> statistics = response.response().stream()
                            .filter(dto -> {
                                if (dto.team() == null) {
                                    logger.warn("⚠️ Skipping statistics with null team data");
                                    return false;
                                }
                                if (dto.team().id() == null || dto.team().id() <= 0) {
                                    logger.warn("⚠️ Skipping statistics with invalid team ID: {}", dto.team());
                                    return false;
                                }
                                return true;
                            })
                            .map(dto -> dto.toDomain(fixtureId))
                            .toList();

                    logger.info("✅ Converted {} statistics to domain models for fixture {}", statistics.size(), fixtureId);
                    return statistics;
                })
                .doOnError(e -> logger.error("❌ Error fetching statistics from API-Football for fixture {}", fixtureId, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch statistics from external API: " + e.getMessage(), e);
                });
    }

    @Override
    public Mono<List<FixtureEvent>> fetchEventsByFixture(Long fixtureId) {
        logger.info("🌐 Fetching events from API-Football endpoint: /fixtures/events?fixture={}", fixtureId);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures/events")
                        .queryParam("fixture", fixtureId)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballEventsResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}, Total events: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.hasErrors()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null || response.response().isEmpty()) {
                        logger.warn("⚠️ Empty response received from API");
                        return List.<FixtureEvent>of();
                    }

                    // Convertir DTOs a modelos de dominio
                    List<FixtureEvent> events = response.response().stream()
                            .map(dto -> dto.toDomain(fixtureId))
                            .toList();

                    // Log resumen de eventos
                    long goals = events.stream().filter(FixtureEvent::isGoal).count();
                    long cards = events.stream().filter(FixtureEvent::isCard).count();
                    long subs = events.stream().filter(FixtureEvent::isSubstitution).count();
                    logger.info("✅ Converted {} events to domain models for fixture {} (Goals: {}, Cards: {}, Subs: {})",
                            events.size(), fixtureId, goals, cards, subs);

                    return events;
                })
                .doOnError(e -> logger.error("❌ Error fetching events from API-Football for fixture {}", fixtureId, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch events from external API: " + e.getMessage(), e);
                });
    }

    @Override
    public Mono<List<MatchLineup>> fetchLineupsByFixture(Long fixtureId) {
        logger.info("🌐 Fetching lineups from API-Football endpoint: /fixtures/lineups?fixture={}", fixtureId);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures/lineups")
                        .queryParam("fixture", fixtureId)
                        .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> {
                        logger.error("❌ HTTP Error: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    logger.error("❌ Error response body: {}", body);
                                    return Mono.error(new RuntimeException("API Error: " + response.statusCode() + " - " + body));
                                });
                    }
                )
                .bodyToMono(ApiFootballLineupsResponse.class)
                .timeout(Duration.ofSeconds(30))
                .doOnNext(response -> {
                    logger.info("✅ API Response received - Results: {}, Teams with lineups: {}",
                            response.results(),
                            response.response() != null ? response.response().size() : 0);
                    logger.debug("📦 Full API Response: {}", response);

                    if (response.hasErrors()) {
                        logger.warn("⚠️ API errors: {}", response.errors());
                    }
                })
                .map(response -> {
                    if (response == null || response.response() == null || response.response().isEmpty()) {
                        logger.warn("⚠️ Empty response received from API");
                        return List.<MatchLineup>of();
                    }

                    // Convertir DTOs a modelos de dominio (aplanar la lista de equipos)
                    List<MatchLineup> lineups = response.response().stream()
                            .filter(dto -> dto.team() != null && dto.team().id() != null)
                            .flatMap(dto -> dto.toDomainList(fixtureId).stream())
                            .toList();

                    // Log resumen de alineaciones
                    long starters = lineups.stream().filter(MatchLineup::isStarter).count();
                    long subs = lineups.stream().filter(l -> !l.isStarter()).count();
                    logger.info("✅ Converted {} players to domain models for fixture {} (Starters: {}, Subs: {})",
                            lineups.size(), fixtureId, starters, subs);

                    return lineups;
                })
                .doOnError(e -> logger.error("❌ Error fetching lineups from API-Football for fixture {}", fixtureId, e))
                .onErrorMap(e -> {
                    logger.error("❌ Detailed error: {}", e.getMessage(), e);
                    return new RuntimeException("Failed to fetch lineups from external API: " + e.getMessage(), e);
                });
    }
}