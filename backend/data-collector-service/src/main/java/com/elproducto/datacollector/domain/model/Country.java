package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para País (Country)
 * Representa un país en el sistema de fútbol
 */
public record Country(
    String name,
    String code,
    String flag
) {
    public Country {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "Country{name='%s', code='%s', flag='%s'}".formatted(name, code, flag);
    }
}