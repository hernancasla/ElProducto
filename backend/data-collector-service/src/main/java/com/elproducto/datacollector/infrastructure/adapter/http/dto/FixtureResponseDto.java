package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.Fixture;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * DTO para mapear cada elemento del array response de fixtures
 *
 * Estructura:
 * {
 *   "fixture": { ... },
 *   "league": { ... },
 *   "teams": { "home": {...}, "away": {...} },
 *   "goals": { "home": 2, "away": 1 },
 *   "score": { "halftime": {...}, "fulltime": {...}, ... }
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureResponseDto(
    @JsonProperty("fixture") FixtureInfoDto fixture,
    @JsonProperty("league") FixtureLeagueDto league,
    @JsonProperty("teams") FixtureTeamsDto teams,
    @JsonProperty("goals") FixtureGoalsDto goals,
    @JsonProperty("score") FixtureScoreDto score
) {
    /**
     * Convierte el DTO a modelo de dominio Fixture
     */
    public Fixture toDomain() {
        if (fixture == null) {
            throw new IllegalStateException("Fixture data cannot be null");
        }

        // Parsear fecha
        LocalDateTime fixtureDate = parseDate(fixture.date());

        // Venue info
        Long venueId = null;
        String venueName = null;
        String venueCity = null;
        if (fixture.venue() != null) {
            venueId = fixture.venue().id();
            venueName = fixture.venue().name();
            venueCity = fixture.venue().city();
        }

        // Status info
        String statusLong = null;
        String statusShort = null;
        Integer elapsed = null;
        if (fixture.status() != null) {
            statusLong = fixture.status().longStatus();
            statusShort = fixture.status().shortStatus();
            elapsed = fixture.status().elapsed();
        }

        // League info
        Long leagueId = null;
        String leagueName = null;
        String leagueCountry = null;
        String leagueLogo = null;
        Integer leagueSeason = null;
        String leagueRound = null;
        if (league != null) {
            leagueId = league.id();
            leagueName = league.name();
            leagueCountry = league.country();
            leagueLogo = league.logo();
            leagueSeason = league.season();
            leagueRound = league.round();
        }

        // Teams info
        Long homeTeamId = null;
        String homeTeamName = null;
        String homeTeamLogo = null;
        Boolean homeTeamWinner = null;
        Long awayTeamId = null;
        String awayTeamName = null;
        String awayTeamLogo = null;
        Boolean awayTeamWinner = null;
        if (teams != null) {
            if (teams.home() != null) {
                homeTeamId = teams.home().id();
                homeTeamName = teams.home().name();
                homeTeamLogo = teams.home().logo();
                homeTeamWinner = teams.home().winner();
            }
            if (teams.away() != null) {
                awayTeamId = teams.away().id();
                awayTeamName = teams.away().name();
                awayTeamLogo = teams.away().logo();
                awayTeamWinner = teams.away().winner();
            }
        }

        // Goals
        Integer goalsHome = goals != null ? goals.home() : null;
        Integer goalsAway = goals != null ? goals.away() : null;

        // Score
        Integer scoreHalftimeHome = null;
        Integer scoreHalftimeAway = null;
        Integer scoreFulltimeHome = null;
        Integer scoreFulltimeAway = null;
        Integer scoreExtratimeHome = null;
        Integer scoreExtratimeAway = null;
        Integer scorePenaltyHome = null;
        Integer scorePenaltyAway = null;
        if (score != null) {
            if (score.halftime() != null) {
                scoreHalftimeHome = score.halftime().home();
                scoreHalftimeAway = score.halftime().away();
            }
            if (score.fulltime() != null) {
                scoreFulltimeHome = score.fulltime().home();
                scoreFulltimeAway = score.fulltime().away();
            }
            if (score.extratime() != null) {
                scoreExtratimeHome = score.extratime().home();
                scoreExtratimeAway = score.extratime().away();
            }
            if (score.penalty() != null) {
                scorePenaltyHome = score.penalty().home();
                scorePenaltyAway = score.penalty().away();
            }
        }

        return new Fixture(
            fixture.id(),
            fixture.referee(),
            fixture.timezone(),
            fixtureDate,
            fixture.timestamp(),
            venueId,
            venueName,
            venueCity,
            statusLong,
            statusShort,
            elapsed,
            leagueId,
            leagueName,
            leagueCountry,
            leagueLogo,
            leagueSeason,
            leagueRound,
            homeTeamId,
            homeTeamName,
            homeTeamLogo,
            homeTeamWinner,
            awayTeamId,
            awayTeamName,
            awayTeamLogo,
            awayTeamWinner,
            goalsHome,
            goalsAway,
            scoreHalftimeHome,
            scoreHalftimeAway,
            scoreFulltimeHome,
            scoreFulltimeAway,
            scoreExtratimeHome,
            scoreExtratimeAway,
            scorePenaltyHome,
            scorePenaltyAway
        );
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            // API-Football returns dates in ISO 8601 format with timezone
            OffsetDateTime odt = OffsetDateTime.parse(dateStr);
            return odt.toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }
}