package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.MatchLineup;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla match_lineups
 * Representa un jugador en la alineación de un partido
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("match_lineups")
public class MatchLineupEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("fixture_id")
    private Long fixtureId;

    // Equipo
    @Column("team_id")
    private Long teamId;

    @Column("team_name")
    private String teamName;

    @Column("team_logo")
    private String teamLogo;

    @Column("formation")
    private String formation;

    // Entrenador
    @Column("coach_id")
    private Long coachId;

    @Column("coach_name")
    private String coachName;

    @Column("coach_photo")
    private String coachPhoto;

    // Jugador
    @Column("player_id")
    private Long playerId;

    @Column("player_name")
    private String playerName;

    @Column("player_number")
    private Integer playerNumber;

    @Column("position")
    private String position;

    @Column("grid")
    private String grid;

    @Column("is_starter")
    private boolean isStarter;

    @Column("created_at")
    private LocalDateTime createdAt;

    public MatchLineupEntity() {
    }

    /**
     * Convierte un modelo de dominio MatchLineup a MatchLineupEntity (nueva entidad)
     */
    public static MatchLineupEntity fromDomain(MatchLineup lineup) {
        MatchLineupEntity entity = new MatchLineupEntity();
        entity.fixtureId = lineup.fixtureId();
        entity.teamId = lineup.teamId();
        entity.teamName = lineup.teamName();
        entity.teamLogo = lineup.teamLogo();
        entity.formation = lineup.formation();
        entity.coachId = lineup.coachId();
        entity.coachName = lineup.coachName();
        entity.coachPhoto = lineup.coachPhoto();
        entity.playerId = lineup.playerId();
        entity.playerName = lineup.playerName();
        entity.playerNumber = lineup.playerNumber();
        entity.position = lineup.position();
        entity.grid = lineup.grid();
        entity.isStarter = lineup.isStarter();
        entity.createdAt = LocalDateTime.now();
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio MatchLineup
     */
    public MatchLineup toDomain() {
        return new MatchLineup(
            fixtureId,
            teamId,
            teamName,
            teamLogo,
            formation,
            coachId,
            coachName,
            coachPhoto,
            playerId,
            playerName,
            playerNumber,
            position,
            grid,
            isStarter
        );
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

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCoachPhoto() {
        return coachPhoto;
    }

    public void setCoachPhoto(String coachPhoto) {
        this.coachPhoto = coachPhoto;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public boolean isStarter() {
        return isStarter;
    }

    public void setStarter(boolean starter) {
        isStarter = starter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MatchLineupEntity{" +
                "id=" + id +
                ", fixtureId=" + fixtureId +
                ", teamName='" + teamName + '\'' +
                ", #" + playerNumber + " " + playerName +
                " (" + position + ")" +
                ", " + (isStarter ? "Titular" : "Suplente") +
                '}';
    }
}