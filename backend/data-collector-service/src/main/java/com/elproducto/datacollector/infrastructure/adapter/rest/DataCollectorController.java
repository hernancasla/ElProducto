package com.elproducto.datacollector.infrastructure.adapter.rest;

import com.elproducto.datacollector.application.usecase.CollectCountriesUseCase;
import com.elproducto.datacollector.application.usecase.CollectFixturesUseCase;
import com.elproducto.datacollector.application.usecase.CollectFixtureEventsUseCase;
import com.elproducto.datacollector.application.usecase.CollectFixtureStatisticsUseCase;
import com.elproducto.datacollector.application.usecase.CollectLeaguesUseCase;
import com.elproducto.datacollector.application.usecase.CollectPlayersUseCase;
import com.elproducto.datacollector.application.usecase.CollectStandingsUseCase;
import com.elproducto.datacollector.application.usecase.CollectTeamsUseCase;
import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.model.Fixture;
import com.elproducto.datacollector.domain.model.FixtureEvent;
import com.elproducto.datacollector.domain.model.FixtureStatistics;
import com.elproducto.datacollector.domain.model.League;
import com.elproducto.datacollector.domain.model.Player;
import com.elproducto.datacollector.domain.model.Standing;
import com.elproducto.datacollector.domain.model.Team;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Controlador REST para ejecutar procesos de recolección de datos
 * Expone endpoints para ejecutar el proceso bajo demanda
 */
@RestController
@RequestMapping("/api/v1/collector")
@Tag(name = "Data Collector", description = "Endpoints para recolección de datos desde API-Football")
public class DataCollectorController {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorController.class);

    private final CollectCountriesUseCase collectCountriesUseCase;
    private final CollectLeaguesUseCase collectLeaguesUseCase;
    private final CollectTeamsUseCase collectTeamsUseCase;
    private final CollectPlayersUseCase collectPlayersUseCase;
    private final CollectFixturesUseCase collectFixturesUseCase;
    private final CollectStandingsUseCase collectStandingsUseCase;
    private final CollectFixtureStatisticsUseCase collectFixtureStatisticsUseCase;
    private final CollectFixtureEventsUseCase collectFixtureEventsUseCase;

    public DataCollectorController(CollectCountriesUseCase collectCountriesUseCase,
                                    CollectLeaguesUseCase collectLeaguesUseCase,
                                    CollectTeamsUseCase collectTeamsUseCase,
                                    CollectPlayersUseCase collectPlayersUseCase,
                                    CollectFixturesUseCase collectFixturesUseCase,
                                    CollectStandingsUseCase collectStandingsUseCase,
                                    CollectFixtureStatisticsUseCase collectFixtureStatisticsUseCase,
                                    CollectFixtureEventsUseCase collectFixtureEventsUseCase) {
        this.collectCountriesUseCase = collectCountriesUseCase;
        this.collectLeaguesUseCase = collectLeaguesUseCase;
        this.collectTeamsUseCase = collectTeamsUseCase;
        this.collectPlayersUseCase = collectPlayersUseCase;
        this.collectFixturesUseCase = collectFixturesUseCase;
        this.collectStandingsUseCase = collectStandingsUseCase;
        this.collectFixtureStatisticsUseCase = collectFixtureStatisticsUseCase;
        this.collectFixtureEventsUseCase = collectFixtureEventsUseCase;
    }

    /**
     * Ejecuta el proceso de recolección de países bajo demanda
     *
     * @return Lista de países recolectados
     */
    @Operation(
        summary = "Recolectar países",
        description = "Ejecuta el proceso de recolección de países desde API-Football. " +
                      "Este endpoint consulta la API externa y devuelve todos los países disponibles " +
                      "con su información (nombre, código ISO y bandera)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Países recolectados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Países recolectados exitosamente",
                          "count": 165,
                          "data": [
                            {
                              "name": "Argentina",
                              "code": "AR",
                              "flag": "https://media.api-sports.io/flags/ar.svg"
                            },
                            {
                              "name": "Brazil",
                              "code": "BR",
                              "flag": "https://media.api-sports.io/flags/br.svg"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CollectionResponse.class),
                examples = @ExampleObject(
                    name = "Error de servidor",
                    value = """
                        {
                          "status": "ERROR",
                          "message": "Error: Connection timeout",
                          "count": 0,
                          "data": []
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/countries")
    public Mono<ResponseEntity<CollectionResponse>> collectCountries() {
        logger.info("🚀 Endpoint /api/v1/collector/countries invocado - Iniciando proceso de recolección");

        return collectCountriesUseCase.execute()
                .map(countries -> {
                    CollectionResponse response = new CollectionResponse(
                            "SUCCESS",
                            "Países recolectados exitosamente",
                            countries.size(),
                            countries
                    );
                    logger.info("✅ Proceso completado exitosamente - {} países recolectados", countries.size());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección", e);
                    CollectionResponse errorResponse = new CollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            List.of()
                    );
                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * Ejecuta el proceso de recolección de ligas para un país específico
     *
     * @param countryCode Código ISO del país (ej: AR, BR, ES)
     * @return Lista de ligas recolectadas con sus temporadas
     */
    @Operation(
        summary = "Recolectar ligas de un país",
        description = "Ejecuta el proceso de recolección de ligas desde API-Football para un país específico. " +
                      "Este endpoint consulta la API externa y devuelve todas las ligas/competiciones del país " +
                      "con su información completa (temporadas, cobertura de datos, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ligas recolectadas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LeagueCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Ligas recolectadas exitosamente para AR",
                          "count": 14,
                          "countryCode": "AR",
                          "data": [
                            {
                              "apiFootballId": 128,
                              "name": "Liga Profesional Argentina",
                              "type": "League",
                              "logo": "https://media.api-sports.io/football/leagues/128.png",
                              "countryCode": "AR",
                              "countryName": "Argentina",
                              "seasons": [
                                {
                                  "year": 2024,
                                  "startDate": "2024-01-26",
                                  "endDate": "2024-12-15",
                                  "current": true,
                                  "coverage": {
                                    "fixturesEvents": true,
                                    "fixturesLineups": true,
                                    "standings": true,
                                    "topScorers": true
                                  }
                                }
                              ]
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Código de país inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LeagueCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = """
                        {
                          "status": "ERROR",
                          "message": "Error: Country code cannot be null or empty",
                          "count": 0,
                          "countryCode": null,
                          "data": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LeagueCollectionResponse.class)
            )
        )
    })
    @PostMapping("/leagues")
    public Mono<ResponseEntity<LeagueCollectionResponse>> collectLeagues(
            @RequestParam(name = "countryCode", required = true) String countryCode) {
        logger.info("🚀 Endpoint /api/v1/collector/leagues invocado - País: {}", countryCode);

        return collectLeaguesUseCase.execute(countryCode)
                .map(leagues -> {
                    LeagueCollectionResponse response = new LeagueCollectionResponse(
                            "SUCCESS",
                            "Ligas recolectadas exitosamente para " + countryCode,
                            leagues.size(),
                            countryCode,
                            leagues
                    );
                    logger.info("✅ Proceso completado exitosamente - {} ligas recolectadas para {}",
                            leagues.size(), countryCode);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de ligas para {}", countryCode, e);
                    LeagueCollectionResponse errorResponse = new LeagueCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            countryCode,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * Ejecuta el proceso de recolección de equipos para un país específico
     *
     * @param country Nombre del país (ej: Argentina, Brazil, Spain)
     * @return Lista de equipos recolectados con sus venues
     */
    @Operation(
        summary = "Recolectar equipos de un país",
        description = "Ejecuta el proceso de recolección de equipos desde API-Football para un país específico. " +
                      "Este endpoint consulta la API externa y devuelve todos los equipos del país " +
                      "con su información completa (estadio, país, año de fundación, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Equipos recolectados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TeamCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Equipos recolectados exitosamente para Argentina",
                          "count": 271,
                          "country": "Argentina",
                          "data": [
                            {
                              "apiFootballId": 26,
                              "name": "Argentina",
                              "code": "ARG",
                              "country": "Argentina",
                              "founded": 1893,
                              "national": true,
                              "logo": "https://media.api-sports.io/football/teams/26.png",
                              "venue": {
                                "apiFootballId": 46,
                                "name": "Estadio Alberto José Armando",
                                "address": "Brandsen 805, La Boca",
                                "city": "Ciudad de Buenos Aires",
                                "capacity": 57200,
                                "surface": "grass",
                                "image": "https://media.api-sports.io/football/venues/46.png"
                              }
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Nombre de país inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TeamCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = """
                        {
                          "status": "ERROR",
                          "message": "Error: Country name cannot be null or empty",
                          "count": 0,
                          "country": null,
                          "data": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TeamCollectionResponse.class)
            )
        )
    })
    @PostMapping("/teams")
    public Mono<ResponseEntity<TeamCollectionResponse>> collectTeams(
            @RequestParam(name = "country", required = true) String country) {
        logger.info("🚀 Endpoint /api/v1/collector/teams invocado - País: {}", country);

        return collectTeamsUseCase.execute(country)
                .map(teams -> {
                    TeamCollectionResponse response = new TeamCollectionResponse(
                            "SUCCESS",
                            "Equipos recolectados exitosamente para " + country,
                            teams.size(),
                            country,
                            teams
                    );
                    logger.info("✅ Proceso completado exitosamente - {} equipos recolectados para {}",
                            teams.size(), country);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de equipos para {}", country, e);
                    TeamCollectionResponse errorResponse = new TeamCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            country,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * DTO para la respuesta del endpoint de países
     */
    public record CollectionResponse(
        String status,
        String message,
        int count,
        List<Country> data
    ) {}

    /**
     * DTO para la respuesta del endpoint de ligas
     */
    public record LeagueCollectionResponse(
        String status,
        String message,
        int count,
        String countryCode,
        List<League> data
    ) {}

    /**
     * DTO para la respuesta del endpoint de equipos
     */
    public record TeamCollectionResponse(
        String status,
        String message,
        int count,
        String country,
        List<Team> data
    ) {}

    /**
     * Ejecuta el proceso de recolección de jugadores para un equipo específico
     *
     * @param teamId ID del equipo en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Lista de jugadores recolectados
     */
    @Operation(
        summary = "Recolectar jugadores de un equipo",
        description = "Ejecuta el proceso de recolección de jugadores desde API-Football para un equipo y temporada específicos. " +
                      "Este endpoint consulta la API externa y devuelve todos los jugadores del equipo " +
                      "con su información completa (nombre, nacionalidad, edad, posición, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Jugadores recolectados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PlayerCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Jugadores recolectados exitosamente para equipo 26 temporada 2024",
                          "count": 28,
                          "teamId": 26,
                          "season": 2024,
                          "data": [
                            {
                              "apiFootballId": 276,
                              "name": "L. Messi",
                              "firstName": "Lionel",
                              "lastName": "Messi",
                              "age": 36,
                              "birthDate": "1987-06-24",
                              "nationality": "Argentina",
                              "height": "170 cm",
                              "weight": "72 kg",
                              "injured": false,
                              "photo": "https://media.api-sports.io/football/players/276.png",
                              "teamId": 26,
                              "teamName": "Argentina"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PlayerCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = """
                        {
                          "status": "ERROR",
                          "message": "Error: Team ID must be positive",
                          "count": 0,
                          "teamId": null,
                          "season": null,
                          "data": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PlayerCollectionResponse.class)
            )
        )
    })
    @PostMapping("/players")
    public Mono<ResponseEntity<PlayerCollectionResponse>> collectPlayers(
            @RequestParam(name = "teamId", required = true) Long teamId,
            @RequestParam(name = "season", required = true) Integer season) {
        logger.info("🚀 Endpoint /api/v1/collector/players invocado - Equipo: {} Temporada: {}", teamId, season);

        return collectPlayersUseCase.execute(teamId, season)
                .map(players -> {
                    PlayerCollectionResponse response = new PlayerCollectionResponse(
                            "SUCCESS",
                            "Jugadores recolectados exitosamente para equipo " + teamId + " temporada " + season,
                            players.size(),
                            teamId,
                            season,
                            players
                    );
                    logger.info("✅ Proceso completado exitosamente - {} jugadores recolectados para equipo {} temporada {}",
                            players.size(), teamId, season);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de jugadores para equipo {} temporada {}", teamId, season, e);
                    PlayerCollectionResponse errorResponse = new PlayerCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            teamId,
                            season,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * DTO para la respuesta del endpoint de jugadores
     */
    public record PlayerCollectionResponse(
        String status,
        String message,
        int count,
        Long teamId,
        Integer season,
        List<Player> data
    ) {}

    /**
     * Ejecuta el proceso de recolección de partidos para una liga específica
     *
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Lista de partidos recolectados
     */
    @Operation(
        summary = "Recolectar partidos de una liga",
        description = "Ejecuta el proceso de recolección de partidos desde API-Football para una liga y temporada específicas. " +
                      "Este endpoint consulta la API externa y devuelve todos los partidos de la liga " +
                      "con su información completa (equipos, resultado, estado, fecha, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Partidos recolectados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Partidos recolectados exitosamente para liga 128 temporada 2024",
                          "count": 380,
                          "leagueId": 128,
                          "season": 2024,
                          "data": [
                            {
                              "apiFootballId": 1234567,
                              "homeTeamName": "Boca Juniors",
                              "awayTeamName": "River Plate",
                              "date": "2024-03-15T18:00:00",
                              "statusShort": "FT",
                              "goalsHome": 2,
                              "goalsAway": 1,
                              "leagueName": "Liga Profesional Argentina",
                              "leagueRound": "Regular Season - 10"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = """
                        {
                          "status": "ERROR",
                          "message": "Error: League ID must be positive",
                          "count": 0,
                          "leagueId": null,
                          "season": null,
                          "data": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureCollectionResponse.class)
            )
        )
    })
    @PostMapping("/fixtures")
    public Mono<ResponseEntity<FixtureCollectionResponse>> collectFixtures(
            @RequestParam(name = "leagueId", required = true) Long leagueId,
            @RequestParam(name = "season", required = true) Integer season) {
        logger.info("🚀 Endpoint /api/v1/collector/fixtures invocado - Liga: {} Temporada: {}", leagueId, season);

        return collectFixturesUseCase.execute(leagueId, season)
                .map(fixtures -> {
                    FixtureCollectionResponse response = new FixtureCollectionResponse(
                            "SUCCESS",
                            "Partidos recolectados exitosamente para liga " + leagueId + " temporada " + season,
                            fixtures.size(),
                            leagueId,
                            season,
                            fixtures
                    );
                    logger.info("✅ Proceso completado exitosamente - {} partidos recolectados para liga {} temporada {}",
                            fixtures.size(), leagueId, season);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de partidos para liga {} temporada {}", leagueId, season, e);
                    FixtureCollectionResponse errorResponse = new FixtureCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            leagueId,
                            season,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * DTO para la respuesta del endpoint de partidos
     */
    public record FixtureCollectionResponse(
        String status,
        String message,
        int count,
        Long leagueId,
        Integer season,
        List<Fixture> data
    ) {}

    /**
     * Ejecuta el proceso de recolección de tabla de posiciones para una liga específica
     *
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Lista de posiciones recolectadas
     */
    @Operation(
        summary = "Recolectar tabla de posiciones de una liga",
        description = "Ejecuta el proceso de recolección de posiciones desde API-Football para una liga y temporada específicas. " +
                      "Este endpoint consulta la API externa y devuelve la tabla de posiciones completa " +
                      "con información de cada equipo (puntos, partidos, goles, forma, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Posiciones recolectadas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandingCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Posiciones recolectadas exitosamente para liga 128 temporada 2024",
                          "count": 28,
                          "leagueId": 128,
                          "season": 2024,
                          "data": [
                            {
                              "rank": 1,
                              "teamId": 435,
                              "teamName": "River Plate",
                              "points": 45,
                              "goalsDiff": 25,
                              "allPlayed": 20,
                              "allWin": 14,
                              "allDraw": 3,
                              "allLose": 3,
                              "form": "WWDWW",
                              "description": "Promotion - Copa Libertadores"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandingCollectionResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StandingCollectionResponse.class)
            )
        )
    })
    @PostMapping("/standings")
    public Mono<ResponseEntity<StandingCollectionResponse>> collectStandings(
            @RequestParam(name = "leagueId", required = true) Long leagueId,
            @RequestParam(name = "season", required = true) Integer season) {
        logger.info("🚀 Endpoint /api/v1/collector/standings invocado - Liga: {} Temporada: {}", leagueId, season);

        return collectStandingsUseCase.execute(leagueId, season)
                .map(standings -> {
                    StandingCollectionResponse response = new StandingCollectionResponse(
                            "SUCCESS",
                            "Posiciones recolectadas exitosamente para liga " + leagueId + " temporada " + season,
                            standings.size(),
                            leagueId,
                            season,
                            standings
                    );
                    logger.info("✅ Proceso completado exitosamente - {} posiciones recolectadas para liga {} temporada {}",
                            standings.size(), leagueId, season);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de posiciones para liga {} temporada {}", leagueId, season, e);
                    StandingCollectionResponse errorResponse = new StandingCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            leagueId,
                            season,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * DTO para la respuesta del endpoint de posiciones
     */
    public record StandingCollectionResponse(
        String status,
        String message,
        int count,
        Long leagueId,
        Integer season,
        List<Standing> data
    ) {}

    /**
     * Ejecuta el proceso de recolección de estadísticas para un partido específico
     *
     * @param fixtureId ID del partido en API-Football
     * @return Lista de estadísticas recolectadas (una por equipo)
     */
    @Operation(
        summary = "Recolectar estadísticas de un partido",
        description = "Ejecuta el proceso de recolección de estadísticas desde API-Football para un partido específico. " +
                      "Este endpoint consulta la API externa y devuelve las estadísticas del partido " +
                      "con información de cada equipo (posesión, tiros, corners, tarjetas, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estadísticas recolectadas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureStatisticsCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Estadísticas recolectadas exitosamente para partido 1234567",
                          "count": 2,
                          "fixtureId": 1234567,
                          "data": [
                            {
                              "fixtureId": 1234567,
                              "teamId": 435,
                              "teamName": "River Plate",
                              "ballPossession": 55,
                              "totalShots": 12,
                              "shotsOnGoal": 5,
                              "cornerKicks": 6,
                              "fouls": 14,
                              "yellowCards": 2,
                              "redCards": 0
                            },
                            {
                              "fixtureId": 1234567,
                              "teamId": 451,
                              "teamName": "Boca Juniors",
                              "ballPossession": 45,
                              "totalShots": 8,
                              "shotsOnGoal": 3,
                              "cornerKicks": 4,
                              "fouls": 18,
                              "yellowCards": 3,
                              "redCards": 1
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureStatisticsCollectionResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureStatisticsCollectionResponse.class)
            )
        )
    })
    @PostMapping("/statistics")
    public Mono<ResponseEntity<FixtureStatisticsCollectionResponse>> collectStatistics(
            @RequestParam(name = "fixtureId", required = true) Long fixtureId) {
        logger.info("🚀 Endpoint /api/v1/collector/statistics invocado - Partido: {}", fixtureId);

        return collectFixtureStatisticsUseCase.execute(fixtureId)
                .map(statistics -> {
                    FixtureStatisticsCollectionResponse response = new FixtureStatisticsCollectionResponse(
                            "SUCCESS",
                            "Estadísticas recolectadas exitosamente para partido " + fixtureId,
                            statistics.size(),
                            fixtureId,
                            statistics
                    );
                    logger.info("✅ Proceso completado exitosamente - {} estadísticas recolectadas para partido {}",
                            statistics.size(), fixtureId);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de estadísticas para partido {}", fixtureId, e);
                    FixtureStatisticsCollectionResponse errorResponse = new FixtureStatisticsCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            fixtureId,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * DTO para la respuesta del endpoint de estadísticas de partido
     */
    public record FixtureStatisticsCollectionResponse(
        String status,
        String message,
        int count,
        Long fixtureId,
        List<FixtureStatistics> data
    ) {}

    /**
     * Ejecuta el proceso de recolección de eventos para un partido específico
     *
     * @param fixtureId ID del partido en API-Football
     * @return Lista de eventos recolectados (goles, tarjetas, sustituciones)
     */
    @Operation(
        summary = "Recolectar eventos de un partido",
        description = "Ejecuta el proceso de recolección de eventos desde API-Football para un partido específico. " +
                      "Este endpoint consulta la API externa y devuelve los eventos del partido " +
                      "(goles, tarjetas amarillas y rojas, sustituciones, etc.) ordenados cronológicamente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Eventos recolectados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureEventsCollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Eventos recolectados exitosamente para partido 1234567",
                          "count": 15,
                          "fixtureId": 1234567,
                          "data": [
                            {
                              "fixtureId": 1234567,
                              "timeElapsed": 23,
                              "timeExtra": null,
                              "teamId": 435,
                              "teamName": "River Plate",
                              "playerId": 276,
                              "playerName": "J. Alvarez",
                              "assistId": 123,
                              "assistName": "N. De La Cruz",
                              "type": "Goal",
                              "detail": "Normal Goal",
                              "comments": null
                            },
                            {
                              "fixtureId": 1234567,
                              "timeElapsed": 45,
                              "timeExtra": 2,
                              "teamId": 451,
                              "teamName": "Boca Juniors",
                              "playerId": 456,
                              "playerName": "E. Fernandez",
                              "type": "Card",
                              "detail": "Yellow Card",
                              "comments": "Foul"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureEventsCollectionResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FixtureEventsCollectionResponse.class)
            )
        )
    })
    @PostMapping("/events")
    public Mono<ResponseEntity<FixtureEventsCollectionResponse>> collectEvents(
            @RequestParam(name = "fixtureId", required = true) Long fixtureId) {
        logger.info("🚀 Endpoint /api/v1/collector/events invocado - Partido: {}", fixtureId);

        return collectFixtureEventsUseCase.execute(fixtureId)
                .map(events -> {
                    FixtureEventsCollectionResponse response = new FixtureEventsCollectionResponse(
                            "SUCCESS",
                            "Eventos recolectados exitosamente para partido " + fixtureId,
                            events.size(),
                            fixtureId,
                            events
                    );
                    logger.info("✅ Proceso completado exitosamente - {} eventos recolectados para partido {}",
                            events.size(), fixtureId);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("❌ Error ejecutando proceso de recolección de eventos para partido {}", fixtureId, e);
                    FixtureEventsCollectionResponse errorResponse = new FixtureEventsCollectionResponse(
                            "ERROR",
                            "Error: " + e.getMessage(),
                            0,
                            fixtureId,
                            List.of()
                    );

                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    }

                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    /**
     * DTO para la respuesta del endpoint de eventos de partido
     */
    public record FixtureEventsCollectionResponse(
        String status,
        String message,
        int count,
        Long fixtureId,
        List<FixtureEvent> data
    ) {}
}