package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.MatchLineup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la alineación de un equipo en un partido
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineupResponseDto(
    LineupTeamDto team,
    String formation,
    List<LineupPlayerWrapperDto> startXI,
    List<LineupPlayerWrapperDto> substitutes,
    LineupCoachDto coach
) {
    /**
     * Convierte este DTO a una lista de modelos de dominio MatchLineup
     * @param fixtureId ID del partido
     * @return Lista de jugadores en la alineación (titulares + suplentes)
     */
    public List<MatchLineup> toDomainList(Long fixtureId) {
        List<MatchLineup> lineups = new ArrayList<>();

        // Agregar titulares
        if (startXI != null) {
            for (LineupPlayerWrapperDto wrapper : startXI) {
                if (wrapper.player() != null) {
                    lineups.add(createLineup(fixtureId, wrapper.player(), true));
                }
            }
        }

        // Agregar suplentes
        if (substitutes != null) {
            for (LineupPlayerWrapperDto wrapper : substitutes) {
                if (wrapper.player() != null) {
                    lineups.add(createLineup(fixtureId, wrapper.player(), false));
                }
            }
        }

        return lineups;
    }

    private MatchLineup createLineup(Long fixtureId, LineupPlayerDto player, boolean isStarter) {
        return new MatchLineup(
            fixtureId,
            team != null ? team.id() : null,
            team != null ? team.name() : null,
            team != null ? team.logo() : null,
            formation,
            coach != null ? coach.id() : null,
            coach != null ? coach.name() : null,
            coach != null ? coach.photo() : null,
            player.id(),
            player.name(),
            player.number(),
            player.pos(),
            player.grid(),
            isStarter
        );
    }
}