package com.elproducto.datacollector.domain.port;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.model.Fixture;
import com.elproducto.datacollector.domain.model.FixtureEvent;
import com.elproducto.datacollector.domain.model.FixtureStatistics;
import com.elproducto.datacollector.domain.model.League;
import com.elproducto.datacollector.domain.model.MatchLineup;
import com.elproducto.datacollector.domain.model.Player;
import com.elproducto.datacollector.domain.model.Standing;
import com.elproducto.datacollector.domain.model.Team;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Puerto de salida (Output Port) - Interfaz para cliente de API externa
 * Define el contrato para obtener datos de la API de fútbol
 */
public interface FootballApiClient {

    /**
     * Obtiene la lista de países disponibles en la API de forma reactiva
     * @return Mono con la lista de países
     */
    Mono<List<Country>> fetchCountries();

    /**
     * Obtiene la lista de ligas de un país específico de forma reactiva
     * @param countryCode Código ISO del país (ej: AR, BR, ES)
     * @return Mono con la lista de ligas
     */
    Mono<List<League>> fetchLeaguesByCountry(String countryCode);

    /**
     * Obtiene la lista de equipos de un país específico de forma reactiva
     * @param country Nombre del país (ej: Argentina, Brazil, Spain)
     * @return Mono con la lista de equipos
     */
    Mono<List<Team>> fetchTeamsByCountry(String country);

    /**
     * Obtiene la lista de jugadores de un equipo para una temporada específica
     * @param teamId ID del equipo en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Mono con la lista de jugadores
     */
    Mono<List<Player>> fetchPlayersByTeam(Long teamId, Integer season);

    /**
     * Obtiene la lista de partidos de una liga para una temporada específica
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Mono con la lista de partidos
     */
    Mono<List<Fixture>> fetchFixturesByLeague(Long leagueId, Integer season);

    /**
     * Obtiene la tabla de posiciones de una liga para una temporada específica
     * @param leagueId ID de la liga en API-Football
     * @param season Año de la temporada (ej: 2024)
     * @return Mono con la lista de posiciones
     */
    Mono<List<Standing>> fetchStandingsByLeague(Long leagueId, Integer season);

    /**
     * Obtiene las estadísticas de un partido específico
     * @param fixtureId ID del partido en API-Football
     * @return Mono con la lista de estadísticas (una por equipo)
     */
    Mono<List<FixtureStatistics>> fetchStatisticsByFixture(Long fixtureId);

    /**
     * Obtiene los eventos de un partido específico (goles, tarjetas, sustituciones)
     * @param fixtureId ID del partido en API-Football
     * @return Mono con la lista de eventos del partido
     */
    Mono<List<FixtureEvent>> fetchEventsByFixture(Long fixtureId);

    /**
     * Obtiene las alineaciones de un partido específico (titulares y suplentes)
     * @param fixtureId ID del partido en API-Football
     * @return Mono con la lista de jugadores en las alineaciones
     */
    Mono<List<MatchLineup>> fetchLineupsByFixture(Long fixtureId);
}