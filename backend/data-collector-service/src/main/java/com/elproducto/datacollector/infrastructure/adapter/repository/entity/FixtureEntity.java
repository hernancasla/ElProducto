package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Fixture;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla fixtures
 * Representa la estructura de datos de partidos en PostgreSQL
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("fixtures")
public class FixtureEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("api_football_id")
    private Long apiFootballId;

    @Column("referee")
    private String referee;

    @Column("timezone")
    private String timezone;

    @Column("fixture_date")
    private LocalDateTime fixtureDate;

    @Column("timestamp")
    private Long timestamp;

    // Venue
    @Column("venue_id")
    private Long venueId;

    @Column("venue_name")
    private String venueName;

    @Column("venue_city")
    private String venueCity;

    // Status
    @Column("status_long")
    private String statusLong;

    @Column("status_short")
    private String statusShort;

    @Column("elapsed")
    private Integer elapsed;

    // League
    @Column("league_id")
    private Long leagueId;

    @Column("league_name")
    private String leagueName;

    @Column("league_country")
    private String leagueCountry;

    @Column("league_logo")
    private String leagueLogo;

    @Column("league_season")
    private Integer leagueSeason;

    @Column("league_round")
    private String leagueRound;

    // Home team
    @Column("home_team_id")
    private Long homeTeamId;

    @Column("home_team_name")
    private String homeTeamName;

    @Column("home_team_logo")
    private String homeTeamLogo;

    @Column("home_team_winner")
    private Boolean homeTeamWinner;

    // Away team
    @Column("away_team_id")
    private Long awayTeamId;

    @Column("away_team_name")
    private String awayTeamName;

    @Column("away_team_logo")
    private String awayTeamLogo;

    @Column("away_team_winner")
    private Boolean awayTeamWinner;

    // Goals
    @Column("goals_home")
    private Integer goalsHome;

    @Column("goals_away")
    private Integer goalsAway;

    // Score
    @Column("score_halftime_home")
    private Integer scoreHalftimeHome;

    @Column("score_halftime_away")
    private Integer scoreHalftimeAway;

    @Column("score_fulltime_home")
    private Integer scoreFulltimeHome;

    @Column("score_fulltime_away")
    private Integer scoreFulltimeAway;

    @Column("score_extratime_home")
    private Integer scoreExtratimeHome;

    @Column("score_extratime_away")
    private Integer scoreExtratimeAway;

    @Column("score_penalty_home")
    private Integer scorePenaltyHome;

    @Column("score_penalty_away")
    private Integer scorePenaltyAway;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public FixtureEntity() {
    }

    /**
     * Convierte un modelo de dominio Fixture a FixtureEntity (nueva entidad)
     */
    public static FixtureEntity fromDomain(Fixture fixture) {
        LocalDateTime now = LocalDateTime.now();
        FixtureEntity entity = new FixtureEntity();
        entity.apiFootballId = fixture.apiFootballId();
        entity.referee = fixture.referee();
        entity.timezone = fixture.timezone();
        entity.fixtureDate = fixture.date();
        entity.timestamp = fixture.timestamp();
        entity.venueId = fixture.venueId();
        entity.venueName = fixture.venueName();
        entity.venueCity = fixture.venueCity();
        entity.statusLong = fixture.statusLong();
        entity.statusShort = fixture.statusShort();
        entity.elapsed = fixture.elapsed();
        entity.leagueId = fixture.leagueId();
        entity.leagueName = fixture.leagueName();
        entity.leagueCountry = fixture.leagueCountry();
        entity.leagueLogo = fixture.leagueLogo();
        entity.leagueSeason = fixture.leagueSeason();
        entity.leagueRound = fixture.leagueRound();
        entity.homeTeamId = fixture.homeTeamId();
        entity.homeTeamName = fixture.homeTeamName();
        entity.homeTeamLogo = fixture.homeTeamLogo();
        entity.homeTeamWinner = fixture.homeTeamWinner();
        entity.awayTeamId = fixture.awayTeamId();
        entity.awayTeamName = fixture.awayTeamName();
        entity.awayTeamLogo = fixture.awayTeamLogo();
        entity.awayTeamWinner = fixture.awayTeamWinner();
        entity.goalsHome = fixture.goalsHome();
        entity.goalsAway = fixture.goalsAway();
        entity.scoreHalftimeHome = fixture.scoreHalftimeHome();
        entity.scoreHalftimeAway = fixture.scoreHalftimeAway();
        entity.scoreFulltimeHome = fixture.scoreFulltimeHome();
        entity.scoreFulltimeAway = fixture.scoreFulltimeAway();
        entity.scoreExtratimeHome = fixture.scoreExtratimeHome();
        entity.scoreExtratimeAway = fixture.scoreExtratimeAway();
        entity.scorePenaltyHome = fixture.scorePenaltyHome();
        entity.scorePenaltyAway = fixture.scorePenaltyAway();
        entity.createdAt = now;
        entity.updatedAt = now;
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Fixture
     */
    public Fixture toDomain() {
        return new Fixture(
            apiFootballId,
            referee,
            timezone,
            fixtureDate,
            timestamp,
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

    /**
     * Actualiza los campos de esta entidad con los valores del fixture
     */
    public void updateFrom(Fixture fixture) {
        this.referee = fixture.referee();
        this.timezone = fixture.timezone();
        this.fixtureDate = fixture.date();
        this.timestamp = fixture.timestamp();
        this.venueId = fixture.venueId();
        this.venueName = fixture.venueName();
        this.venueCity = fixture.venueCity();
        this.statusLong = fixture.statusLong();
        this.statusShort = fixture.statusShort();
        this.elapsed = fixture.elapsed();
        this.leagueId = fixture.leagueId();
        this.leagueName = fixture.leagueName();
        this.leagueCountry = fixture.leagueCountry();
        this.leagueLogo = fixture.leagueLogo();
        this.leagueSeason = fixture.leagueSeason();
        this.leagueRound = fixture.leagueRound();
        this.homeTeamId = fixture.homeTeamId();
        this.homeTeamName = fixture.homeTeamName();
        this.homeTeamLogo = fixture.homeTeamLogo();
        this.homeTeamWinner = fixture.homeTeamWinner();
        this.awayTeamId = fixture.awayTeamId();
        this.awayTeamName = fixture.awayTeamName();
        this.awayTeamLogo = fixture.awayTeamLogo();
        this.awayTeamWinner = fixture.awayTeamWinner();
        this.goalsHome = fixture.goalsHome();
        this.goalsAway = fixture.goalsAway();
        this.scoreHalftimeHome = fixture.scoreHalftimeHome();
        this.scoreHalftimeAway = fixture.scoreHalftimeAway();
        this.scoreFulltimeHome = fixture.scoreFulltimeHome();
        this.scoreFulltimeAway = fixture.scoreFulltimeAway();
        this.scoreExtratimeHome = fixture.scoreExtratimeHome();
        this.scoreExtratimeAway = fixture.scoreExtratimeAway();
        this.scorePenaltyHome = fixture.scorePenaltyHome();
        this.scorePenaltyAway = fixture.scorePenaltyAway();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca esta entidad como no nueva (ya existe en la BD)
     */
    public void markAsNotNew() {
        this.isNew = false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    // Getters y Setters
    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiFootballId() {
        return apiFootballId;
    }

    public void setApiFootballId(Long apiFootballId) {
        this.apiFootballId = apiFootballId;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public LocalDateTime getFixtureDate() {
        return fixtureDate;
    }

    public void setFixtureDate(LocalDateTime fixtureDate) {
        this.fixtureDate = fixtureDate;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public String getStatusLong() {
        return statusLong;
    }

    public void setStatusLong(String statusLong) {
        this.statusLong = statusLong;
    }

    public String getStatusShort() {
        return statusShort;
    }

    public void setStatusShort(String statusShort) {
        this.statusShort = statusShort;
    }

    public Integer getElapsed() {
        return elapsed;
    }

    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueCountry() {
        return leagueCountry;
    }

    public void setLeagueCountry(String leagueCountry) {
        this.leagueCountry = leagueCountry;
    }

    public String getLeagueLogo() {
        return leagueLogo;
    }

    public void setLeagueLogo(String leagueLogo) {
        this.leagueLogo = leagueLogo;
    }

    public Integer getLeagueSeason() {
        return leagueSeason;
    }

    public void setLeagueSeason(Integer leagueSeason) {
        this.leagueSeason = leagueSeason;
    }

    public String getLeagueRound() {
        return leagueRound;
    }

    public void setLeagueRound(String leagueRound) {
        this.leagueRound = leagueRound;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getHomeTeamLogo() {
        return homeTeamLogo;
    }

    public void setHomeTeamLogo(String homeTeamLogo) {
        this.homeTeamLogo = homeTeamLogo;
    }

    public Boolean getHomeTeamWinner() {
        return homeTeamWinner;
    }

    public void setHomeTeamWinner(Boolean homeTeamWinner) {
        this.homeTeamWinner = homeTeamWinner;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public String getAwayTeamLogo() {
        return awayTeamLogo;
    }

    public void setAwayTeamLogo(String awayTeamLogo) {
        this.awayTeamLogo = awayTeamLogo;
    }

    public Boolean getAwayTeamWinner() {
        return awayTeamWinner;
    }

    public void setAwayTeamWinner(Boolean awayTeamWinner) {
        this.awayTeamWinner = awayTeamWinner;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public Integer getScoreHalftimeHome() {
        return scoreHalftimeHome;
    }

    public void setScoreHalftimeHome(Integer scoreHalftimeHome) {
        this.scoreHalftimeHome = scoreHalftimeHome;
    }

    public Integer getScoreHalftimeAway() {
        return scoreHalftimeAway;
    }

    public void setScoreHalftimeAway(Integer scoreHalftimeAway) {
        this.scoreHalftimeAway = scoreHalftimeAway;
    }

    public Integer getScoreFulltimeHome() {
        return scoreFulltimeHome;
    }

    public void setScoreFulltimeHome(Integer scoreFulltimeHome) {
        this.scoreFulltimeHome = scoreFulltimeHome;
    }

    public Integer getScoreFulltimeAway() {
        return scoreFulltimeAway;
    }

    public void setScoreFulltimeAway(Integer scoreFulltimeAway) {
        this.scoreFulltimeAway = scoreFulltimeAway;
    }

    public Integer getScoreExtratimeHome() {
        return scoreExtratimeHome;
    }

    public void setScoreExtratimeHome(Integer scoreExtratimeHome) {
        this.scoreExtratimeHome = scoreExtratimeHome;
    }

    public Integer getScoreExtratimeAway() {
        return scoreExtratimeAway;
    }

    public void setScoreExtratimeAway(Integer scoreExtratimeAway) {
        this.scoreExtratimeAway = scoreExtratimeAway;
    }

    public Integer getScorePenaltyHome() {
        return scorePenaltyHome;
    }

    public void setScorePenaltyHome(Integer scorePenaltyHome) {
        this.scorePenaltyHome = scorePenaltyHome;
    }

    public Integer getScorePenaltyAway() {
        return scorePenaltyAway;
    }

    public void setScorePenaltyAway(Integer scorePenaltyAway) {
        this.scorePenaltyAway = scorePenaltyAway;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "FixtureEntity{" +
                "id=" + id +
                ", apiFootballId=" + apiFootballId +
                ", " + homeTeamName + " vs " + awayTeamName +
                ", date=" + fixtureDate +
                ", status=" + statusShort +
                '}';
    }
}