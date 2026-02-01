package com.elproducto.datacollector.domain.model;

/**
 * Tipos de entidades sincronizables con su configuracion de frecuencia
 */
public enum EntityType {
    COUNTRIES("countries", SyncFrequency.MANUAL),
    LEAGUES("leagues", SyncFrequency.WEEKLY),
    TEAMS("teams", SyncFrequency.WEEKLY),
    PLAYERS("players", SyncFrequency.WEEKLY),
    FIXTURES("fixtures", SyncFrequency.DAILY),
    STANDINGS("standings", SyncFrequency.EVENT_DRIVEN),
    FIXTURE_STATISTICS("fixture_statistics", SyncFrequency.EVENT_DRIVEN),
    FIXTURE_EVENTS("fixture_events", SyncFrequency.EVENT_DRIVEN),
    MATCH_LINEUPS("match_lineups", SyncFrequency.EVENT_DRIVEN);

    private final String tableName;
    private final SyncFrequency defaultFrequency;

    EntityType(String tableName, SyncFrequency defaultFrequency) {
        this.tableName = tableName;
        this.defaultFrequency = defaultFrequency;
    }

    public String getTableName() {
        return tableName;
    }

    public SyncFrequency getDefaultFrequency() {
        return defaultFrequency;
    }

    /**
     * Obtiene el EntityType a partir del nombre de tabla
     */
    public static EntityType fromTableName(String tableName) {
        for (EntityType type : values()) {
            if (type.tableName.equals(tableName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown table name: " + tableName);
    }
}