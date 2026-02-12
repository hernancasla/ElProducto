package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.FixtureEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para un evento de partido
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventResponseDto(
    EventTimeDto time,
    EventTeamDto team,
    EventPlayerDto player,
    EventPlayerDto assist,
    String type,
    String detail,
    String comments
) {
    /**
     * Convierte este DTO a un modelo de dominio FixtureEvent
     * @param fixtureId ID del partido
     * @return Modelo de dominio FixtureEvent
     */
    public FixtureEvent toDomain(Long fixtureId) {
        return new FixtureEvent(
            fixtureId,
            time != null ? time.elapsed() : null,
            time != null ? time.extra() : null,
            team != null ? team.id() : null,
            team != null ? team.name() : null,
            team != null ? team.logo() : null,
            player != null ? player.id() : null,
            player != null ? player.name() : null,
            assist != null ? assist.id() : null,
            assist != null ? assist.name() : null,
            type,
            detail,
            comments
        );
    }
}