package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Standing;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla standings
 * Representa la estructura de datos de posiciones en PostgreSQL
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("standings")
public class StandingEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    // League info
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

    // Standing info
    @Column("rank")
    private Integer rank;

    @Column("team_id")
    private Long teamId;

    @Column("team_name")
    private String teamName;

    @Column("team_logo")
    private String teamLogo;

    @Column("points")
    private Integer points;

    @Column("goals_diff")
    private Integer goalsDiff;

    @Column("group_name")
    private String groupName;

    @Column("form")
    private String form;

    @Column("status")
    private String status;

    @Column("description")
    private String description;

    // All stats
    @Column("all_played")
    private Integer allPlayed;

    @Column("all_win")
    private Integer allWin;

    @Column("all_draw")
    private Integer allDraw;

    @Column("all_lose")
    private Integer allLose;

    @Column("all_goals_for")
    private Integer allGoalsFor;

    @Column("all_goals_against")
    private Integer allGoalsAgainst;

    // Home stats
    @Column("home_played")
    private Integer homePlayed;

    @Column("home_win")
    private Integer homeWin;

    @Column("home_draw")
    private Integer homeDraw;

    @Column("home_lose")
    private Integer homeLose;

    @Column("home_goals_for")
    private Integer homeGoalsFor;

    @Column("home_goals_against")
    private Integer homeGoalsAgainst;

    // Away stats
    @Column("away_played")
    private Integer awayPlayed;

    @Column("away_win")
    private Integer awayWin;

    @Column("away_draw")
    private Integer awayDraw;

    @Column("away_lose")
    private Integer awayLose;

    @Column("away_goals_for")
    private Integer awayGoalsFor;

    @Column("away_goals_against")
    private Integer awayGoalsAgainst;

    @Column("last_update")
    private String lastUpdate;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public StandingEntity() {
    }

    /**
     * Convierte un modelo de dominio Standing a StandingEntity
     */
    public static StandingEntity fromDomain(Standing standing) {
        LocalDateTime now = LocalDateTime.now();
        StandingEntity entity = new StandingEntity();
        entity.leagueId = standing.leagueId();
        entity.leagueName = standing.leagueName();
        entity.leagueCountry = standing.leagueCountry();
        entity.leagueLogo = standing.leagueLogo();
        entity.leagueSeason = standing.leagueSeason();
        entity.rank = standing.rank();
        entity.teamId = standing.teamId();
        entity.teamName = standing.teamName();
        entity.teamLogo = standing.teamLogo();
        entity.points = standing.points();
        entity.goalsDiff = standing.goalsDiff();
        entity.groupName = standing.group();
        entity.form = standing.form();
        entity.status = standing.status();
        entity.description = standing.description();
        entity.allPlayed = standing.allPlayed();
        entity.allWin = standing.allWin();
        entity.allDraw = standing.allDraw();
        entity.allLose = standing.allLose();
        entity.allGoalsFor = standing.allGoalsFor();
        entity.allGoalsAgainst = standing.allGoalsAgainst();
        entity.homePlayed = standing.homePlayed();
        entity.homeWin = standing.homeWin();
        entity.homeDraw = standing.homeDraw();
        entity.homeLose = standing.homeLose();
        entity.homeGoalsFor = standing.homeGoalsFor();
        entity.homeGoalsAgainst = standing.homeGoalsAgainst();
        entity.awayPlayed = standing.awayPlayed();
        entity.awayWin = standing.awayWin();
        entity.awayDraw = standing.awayDraw();
        entity.awayLose = standing.awayLose();
        entity.awayGoalsFor = standing.awayGoalsFor();
        entity.awayGoalsAgainst = standing.awayGoalsAgainst();
        entity.lastUpdate = standing.lastUpdate();
        entity.createdAt = now;
        entity.updatedAt = now;
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Standing
     */
    public Standing toDomain() {
        return new Standing(
            leagueId,
            leagueName,
            leagueCountry,
            leagueLogo,
            leagueSeason,
            rank,
            teamId,
            teamName,
            teamLogo,
            points,
            goalsDiff,
            groupName,
            form,
            status,
            description,
            allPlayed,
            allWin,
            allDraw,
            allLose,
            allGoalsFor,
            allGoalsAgainst,
            homePlayed,
            homeWin,
            homeDraw,
            homeLose,
            homeGoalsFor,
            homeGoalsAgainst,
            awayPlayed,
            awayWin,
            awayDraw,
            awayLose,
            awayGoalsFor,
            awayGoalsAgainst,
            lastUpdate
        );
    }

    /**
     * Actualiza los campos de esta entidad con los valores del standing
     */
    public void updateFrom(Standing standing) {
        this.leagueName = standing.leagueName();
        this.leagueCountry = standing.leagueCountry();
        this.leagueLogo = standing.leagueLogo();
        this.rank = standing.rank();
        this.teamName = standing.teamName();
        this.teamLogo = standing.teamLogo();
        this.points = standing.points();
        this.goalsDiff = standing.goalsDiff();
        this.groupName = standing.group();
        this.form = standing.form();
        this.status = standing.status();
        this.description = standing.description();
        this.allPlayed = standing.allPlayed();
        this.allWin = standing.allWin();
        this.allDraw = standing.allDraw();
        this.allLose = standing.allLose();
        this.allGoalsFor = standing.allGoalsFor();
        this.allGoalsAgainst = standing.allGoalsAgainst();
        this.homePlayed = standing.homePlayed();
        this.homeWin = standing.homeWin();
        this.homeDraw = standing.homeDraw();
        this.homeLose = standing.homeLose();
        this.homeGoalsFor = standing.homeGoalsFor();
        this.homeGoalsAgainst = standing.homeGoalsAgainst();
        this.awayPlayed = standing.awayPlayed();
        this.awayWin = standing.awayWin();
        this.awayDraw = standing.awayDraw();
        this.awayLose = standing.awayLose();
        this.awayGoalsFor = standing.awayGoalsFor();
        this.awayGoalsAgainst = standing.awayGoalsAgainst();
        this.lastUpdate = standing.lastUpdate();
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getGoalsDiff() {
        return goalsDiff;
    }

    public void setGoalsDiff(Integer goalsDiff) {
        this.goalsDiff = goalsDiff;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAllPlayed() {
        return allPlayed;
    }

    public void setAllPlayed(Integer allPlayed) {
        this.allPlayed = allPlayed;
    }

    public Integer getAllWin() {
        return allWin;
    }

    public void setAllWin(Integer allWin) {
        this.allWin = allWin;
    }

    public Integer getAllDraw() {
        return allDraw;
    }

    public void setAllDraw(Integer allDraw) {
        this.allDraw = allDraw;
    }

    public Integer getAllLose() {
        return allLose;
    }

    public void setAllLose(Integer allLose) {
        this.allLose = allLose;
    }

    public Integer getAllGoalsFor() {
        return allGoalsFor;
    }

    public void setAllGoalsFor(Integer allGoalsFor) {
        this.allGoalsFor = allGoalsFor;
    }

    public Integer getAllGoalsAgainst() {
        return allGoalsAgainst;
    }

    public void setAllGoalsAgainst(Integer allGoalsAgainst) {
        this.allGoalsAgainst = allGoalsAgainst;
    }

    public Integer getHomePlayed() {
        return homePlayed;
    }

    public void setHomePlayed(Integer homePlayed) {
        this.homePlayed = homePlayed;
    }

    public Integer getHomeWin() {
        return homeWin;
    }

    public void setHomeWin(Integer homeWin) {
        this.homeWin = homeWin;
    }

    public Integer getHomeDraw() {
        return homeDraw;
    }

    public void setHomeDraw(Integer homeDraw) {
        this.homeDraw = homeDraw;
    }

    public Integer getHomeLose() {
        return homeLose;
    }

    public void setHomeLose(Integer homeLose) {
        this.homeLose = homeLose;
    }

    public Integer getHomeGoalsFor() {
        return homeGoalsFor;
    }

    public void setHomeGoalsFor(Integer homeGoalsFor) {
        this.homeGoalsFor = homeGoalsFor;
    }

    public Integer getHomeGoalsAgainst() {
        return homeGoalsAgainst;
    }

    public void setHomeGoalsAgainst(Integer homeGoalsAgainst) {
        this.homeGoalsAgainst = homeGoalsAgainst;
    }

    public Integer getAwayPlayed() {
        return awayPlayed;
    }

    public void setAwayPlayed(Integer awayPlayed) {
        this.awayPlayed = awayPlayed;
    }

    public Integer getAwayWin() {
        return awayWin;
    }

    public void setAwayWin(Integer awayWin) {
        this.awayWin = awayWin;
    }

    public Integer getAwayDraw() {
        return awayDraw;
    }

    public void setAwayDraw(Integer awayDraw) {
        this.awayDraw = awayDraw;
    }

    public Integer getAwayLose() {
        return awayLose;
    }

    public void setAwayLose(Integer awayLose) {
        this.awayLose = awayLose;
    }

    public Integer getAwayGoalsFor() {
        return awayGoalsFor;
    }

    public void setAwayGoalsFor(Integer awayGoalsFor) {
        this.awayGoalsFor = awayGoalsFor;
    }

    public Integer getAwayGoalsAgainst() {
        return awayGoalsAgainst;
    }

    public void setAwayGoalsAgainst(Integer awayGoalsAgainst) {
        this.awayGoalsAgainst = awayGoalsAgainst;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
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
        return "StandingEntity{" +
                "rank=" + rank +
                ", team='" + teamName + '\'' +
                ", points=" + points +
                ", league='" + leagueName + '\'' +
                ", season=" + leagueSeason +
                '}';
    }
}