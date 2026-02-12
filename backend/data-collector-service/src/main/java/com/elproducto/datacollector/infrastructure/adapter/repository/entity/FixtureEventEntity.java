package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.FixtureEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla fixture_events
 * Representa un evento dentro de un partido
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("fixture_events")
public class FixtureEventEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("fixture_id")
    private Long fixtureId;

    // Tiempo
    @Column("time_elapsed")
    private Integer timeElapsed;

    @Column("time_extra")
    private Integer timeExtra;

    // Equipo
    @Column("team_id")
    private Long teamId;

    @Column("team_name")
    private String teamName;

    @Column("team_logo")
    private String teamLogo;

    // Jugador principal
    @Column("player_id")
    private Long playerId;

    @Column("player_name")
    private String playerName;

    // Jugador secundario (asistencia o sustituido)
    @Column("assist_id")
    private Long assistId;

    @Column("assist_name")
    private String assistName;

    // Tipo de evento
    @Column("type")
    private String type;

    @Column("detail")
    private String detail;

    @Column("comments")
    private String comments;

    @Column("created_at")
    private LocalDateTime createdAt;

    public FixtureEventEntity() {
    }

    /**
     * Convierte un modelo de dominio FixtureEvent a FixtureEventEntity (nueva entidad)
     */
    public static FixtureEventEntity fromDomain(FixtureEvent event) {
        FixtureEventEntity entity = new FixtureEventEntity();
        entity.fixtureId = event.fixtureId();
        entity.timeElapsed = event.timeElapsed();
        entity.timeExtra = event.timeExtra();
        entity.teamId = event.teamId();
        entity.teamName = event.teamName();
        entity.teamLogo = event.teamLogo();
        entity.playerId = event.playerId();
        entity.playerName = event.playerName();
        entity.assistId = event.assistId();
        entity.assistName = event.assistName();
        entity.type = event.type();
        entity.detail = event.detail();
        entity.comments = event.comments();
        entity.createdAt = LocalDateTime.now();
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio FixtureEvent
     */
    public FixtureEvent toDomain() {
        return new FixtureEvent(
            fixtureId,
            timeElapsed,
            timeExtra,
            teamId,
            teamName,
            teamLogo,
            playerId,
            playerName,
            assistId,
            assistName,
            type,
            detail,
            comments
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

    public Integer getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(Integer timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public Integer getTimeExtra() {
        return timeExtra;
    }

    public void setTimeExtra(Integer timeExtra) {
        this.timeExtra = timeExtra;
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

    public Long getAssistId() {
        return assistId;
    }

    public void setAssistId(Long assistId) {
        this.assistId = assistId;
    }

    public String getAssistName() {
        return assistName;
    }

    public void setAssistName(String assistName) {
        this.assistName = assistName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        String time = timeExtra != null && timeExtra > 0 ? timeElapsed + "+" + timeExtra : String.valueOf(timeElapsed);
        return "FixtureEventEntity{" +
                "id=" + id +
                ", fixtureId=" + fixtureId +
                ", " + time + "' " + type + " - " + playerName +
                '}';
    }
}