package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.FixtureStatistics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO para un equipo y sus estadísticas en un partido
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StatisticsResponseDto(
    StatisticsTeamDto team,
    List<StatisticItemDto> statistics
) {
    /**
     * Convierte este DTO a un modelo de dominio FixtureStatistics
     * @param fixtureId ID del partido
     * @return Modelo de dominio FixtureStatistics
     */
    public FixtureStatistics toDomain(Long fixtureId) {
        return new FixtureStatistics(
            fixtureId,
            team.id(),
            team.name(),
            team.logo(),
            getStatValue("Shots on Goal"),
            getStatValue("Shots off Goal"),
            getStatValue("Total Shots"),
            getStatValue("Blocked Shots"),
            getStatValue("Shots insidebox"),
            getStatValue("Shots outsidebox"),
            getStatValue("Fouls"),
            getStatValue("Corner Kicks"),
            getStatValue("Offsides"),
            parsePercentage(getStatStringValue("Ball Possession")),
            getStatValue("Yellow Cards"),
            getStatValue("Red Cards"),
            getStatValue("Goalkeeper Saves"),
            getStatValue("Total passes"),
            getStatValue("Passes accurate"),
            parsePercentage(getStatStringValue("Passes %")),
            parseExpectedGoals(getStatStringValue("expected_goals"))
        );
    }

    /**
     * Obtiene el valor numérico de una estadística por tipo
     */
    private Integer getStatValue(String type) {
        if (statistics == null) return null;
        return statistics.stream()
            .filter(s -> type.equalsIgnoreCase(s.type()))
            .findFirst()
            .map(s -> parseValue(s.value()))
            .orElse(null);
    }

    /**
     * Obtiene el valor como String de una estadística por tipo
     */
    private String getStatStringValue(String type) {
        if (statistics == null) return null;
        return statistics.stream()
            .filter(s -> type.equalsIgnoreCase(s.type()))
            .findFirst()
            .map(s -> s.value() != null ? s.value().toString() : null)
            .orElse(null);
    }

    /**
     * Parsea un valor de estadística a Integer
     */
    private Integer parseValue(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            String strValue = value.toString().replaceAll("[^0-9]", "");
            return strValue.isEmpty() ? null : Integer.parseInt(strValue);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parsea un valor de porcentaje (ej: "65%" -> 65)
     */
    private Integer parsePercentage(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parsea expected goals (puede venir como decimal, ej: "1.25")
     */
    private Integer parseExpectedGoals(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            // Multiplicar por 100 para mantener precisión (1.25 -> 125)
            double xg = Double.parseDouble(value);
            return (int) (xg * 100);
        } catch (Exception e) {
            return null;
        }
    }
}