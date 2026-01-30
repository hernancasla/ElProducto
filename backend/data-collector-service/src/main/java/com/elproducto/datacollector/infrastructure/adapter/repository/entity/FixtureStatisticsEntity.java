package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.FixtureStatistics;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla fixture_statistics
 * Representa las estadísticas de un equipo en un partido
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("fixture_statistics")
public class FixtureStatisticsEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("fixture_id")
    private Long fixtureId;

    @Column("team_id")
    private Long teamId;

    @Column("team_name")
    private String teamName;

    @Column("team_logo")
    private String teamLogo;

    // Estadísticas de tiros
    @Column("shots_on_goal")
    private Integer shotsOnGoal;

    @Column("shots_off_goal")
    private Integer shotsOffGoal;

    @Column("total_shots")
    private Integer totalShots;

    @Column("blocked_shots")
    private Integer blockedShots;

    @Column("shots_inside_box")
    private Integer shotsInsideBox;

    @Column("shots_outside_box")
    private Integer shotsOutsideBox;

    // Estadísticas de juego
    @Column("fouls")
    private Integer fouls;

    @Column("corner_kicks")
    private Integer cornerKicks;

    @Column("offsides")
    private Integer offsides;

    @Column("ball_possession")
    private Integer ballPossession;

    // Tarjetas
    @Column("yellow_cards")
    private Integer yellowCards;

    @Column("red_cards")
    private Integer redCards;

    // Portero
    @Column("goalkeeper_saves")
    private Integer goalkeeperSaves;

    // Pases
    @Column("total_passes")
    private Integer totalPasses;

    @Column("passes_accurate")
    private Integer passesAccurate;

    @Column("passes_percentage")
    private Integer passesPercentage;

    // Otros
    @Column("expected_goals")
    private Integer expectedGoals;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public FixtureStatisticsEntity() {
    }

    /**
     * Convierte un modelo de dominio FixtureStatistics a FixtureStatisticsEntity (nueva entidad)
     */
    public static FixtureStatisticsEntity fromDomain(FixtureStatistics statistics) {
        LocalDateTime now = LocalDateTime.now();
        FixtureStatisticsEntity entity = new FixtureStatisticsEntity();
        entity.fixtureId = statistics.fixtureId();
        entity.teamId = statistics.teamId();
        entity.teamName = statistics.teamName();
        entity.teamLogo = statistics.teamLogo();
        entity.shotsOnGoal = statistics.shotsOnGoal();
        entity.shotsOffGoal = statistics.shotsOffGoal();
        entity.totalShots = statistics.totalShots();
        entity.blockedShots = statistics.blockedShots();
        entity.shotsInsideBox = statistics.shotsInsideBox();
        entity.shotsOutsideBox = statistics.shotsOutsideBox();
        entity.fouls = statistics.fouls();
        entity.cornerKicks = statistics.cornerKicks();
        entity.offsides = statistics.offsides();
        entity.ballPossession = statistics.ballPossession();
        entity.yellowCards = statistics.yellowCards();
        entity.redCards = statistics.redCards();
        entity.goalkeeperSaves = statistics.goalkeeperSaves();
        entity.totalPasses = statistics.totalPasses();
        entity.passesAccurate = statistics.passesAccurate();
        entity.passesPercentage = statistics.passesPercentage();
        entity.expectedGoals = statistics.expectedGoals();
        entity.createdAt = now;
        entity.updatedAt = now;
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio FixtureStatistics
     */
    public FixtureStatistics toDomain() {
        return new FixtureStatistics(
            fixtureId,
            teamId,
            teamName,
            teamLogo,
            shotsOnGoal,
            shotsOffGoal,
            totalShots,
            blockedShots,
            shotsInsideBox,
            shotsOutsideBox,
            fouls,
            cornerKicks,
            offsides,
            ballPossession,
            yellowCards,
            redCards,
            goalkeeperSaves,
            totalPasses,
            passesAccurate,
            passesPercentage,
            expectedGoals
        );
    }

    /**
     * Actualiza los campos de esta entidad con los valores de las estadísticas
     */
    public void updateFrom(FixtureStatistics statistics) {
        this.teamName = statistics.teamName();
        this.teamLogo = statistics.teamLogo();
        this.shotsOnGoal = statistics.shotsOnGoal();
        this.shotsOffGoal = statistics.shotsOffGoal();
        this.totalShots = statistics.totalShots();
        this.blockedShots = statistics.blockedShots();
        this.shotsInsideBox = statistics.shotsInsideBox();
        this.shotsOutsideBox = statistics.shotsOutsideBox();
        this.fouls = statistics.fouls();
        this.cornerKicks = statistics.cornerKicks();
        this.offsides = statistics.offsides();
        this.ballPossession = statistics.ballPossession();
        this.yellowCards = statistics.yellowCards();
        this.redCards = statistics.redCards();
        this.goalkeeperSaves = statistics.goalkeeperSaves();
        this.totalPasses = statistics.totalPasses();
        this.passesAccurate = statistics.passesAccurate();
        this.passesPercentage = statistics.passesPercentage();
        this.expectedGoals = statistics.expectedGoals();
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

    public Long getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(Long fixtureId) {
        this.fixtureId = fixtureId;
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

    public Integer getShotsOnGoal() {
        return shotsOnGoal;
    }

    public void setShotsOnGoal(Integer shotsOnGoal) {
        this.shotsOnGoal = shotsOnGoal;
    }

    public Integer getShotsOffGoal() {
        return shotsOffGoal;
    }

    public void setShotsOffGoal(Integer shotsOffGoal) {
        this.shotsOffGoal = shotsOffGoal;
    }

    public Integer getTotalShots() {
        return totalShots;
    }

    public void setTotalShots(Integer totalShots) {
        this.totalShots = totalShots;
    }

    public Integer getBlockedShots() {
        return blockedShots;
    }

    public void setBlockedShots(Integer blockedShots) {
        this.blockedShots = blockedShots;
    }

    public Integer getShotsInsideBox() {
        return shotsInsideBox;
    }

    public void setShotsInsideBox(Integer shotsInsideBox) {
        this.shotsInsideBox = shotsInsideBox;
    }

    public Integer getShotsOutsideBox() {
        return shotsOutsideBox;
    }

    public void setShotsOutsideBox(Integer shotsOutsideBox) {
        this.shotsOutsideBox = shotsOutsideBox;
    }

    public Integer getFouls() {
        return fouls;
    }

    public void setFouls(Integer fouls) {
        this.fouls = fouls;
    }

    public Integer getCornerKicks() {
        return cornerKicks;
    }

    public void setCornerKicks(Integer cornerKicks) {
        this.cornerKicks = cornerKicks;
    }

    public Integer getOffsides() {
        return offsides;
    }

    public void setOffsides(Integer offsides) {
        this.offsides = offsides;
    }

    public Integer getBallPossession() {
        return ballPossession;
    }

    public void setBallPossession(Integer ballPossession) {
        this.ballPossession = ballPossession;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    public Integer getGoalkeeperSaves() {
        return goalkeeperSaves;
    }

    public void setGoalkeeperSaves(Integer goalkeeperSaves) {
        this.goalkeeperSaves = goalkeeperSaves;
    }

    public Integer getTotalPasses() {
        return totalPasses;
    }

    public void setTotalPasses(Integer totalPasses) {
        this.totalPasses = totalPasses;
    }

    public Integer getPassesAccurate() {
        return passesAccurate;
    }

    public void setPassesAccurate(Integer passesAccurate) {
        this.passesAccurate = passesAccurate;
    }

    public Integer getPassesPercentage() {
        return passesPercentage;
    }

    public void setPassesPercentage(Integer passesPercentage) {
        this.passesPercentage = passesPercentage;
    }

    public Integer getExpectedGoals() {
        return expectedGoals;
    }

    public void setExpectedGoals(Integer expectedGoals) {
        this.expectedGoals = expectedGoals;
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
        return "FixtureStatisticsEntity{" +
                "id=" + id +
                ", fixtureId=" + fixtureId +
                ", team=" + teamName +
                ", possession=" + ballPossession + "%" +
                ", shots=" + totalShots +
                '}';
    }
}