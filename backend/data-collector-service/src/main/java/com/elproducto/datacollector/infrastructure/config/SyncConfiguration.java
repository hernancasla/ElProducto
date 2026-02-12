package com.elproducto.datacollector.infrastructure.config;

import com.elproducto.datacollector.domain.model.EntityType;
import com.elproducto.datacollector.domain.model.SyncFrequency;
import com.elproducto.datacollector.domain.model.SyncStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuracion de sincronizacion para cada entidad
 * Se puede personalizar via application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "sync")
public class SyncConfiguration {

    private boolean enabled = false;
    private Duration rateLimitDelay = Duration.ofMillis(200);
    private int maxRetries = 3;
    private Duration staleTimeout = Duration.ofHours(1);
    private Map<String, EntitySyncConfig> entities = new HashMap<>();

    public SyncConfiguration() {
        // Configuracion por defecto para cada entidad
        initializeDefaults();
    }

    private void initializeDefaults() {
        // Countries - Solo manual
        entities.put("countries", new EntitySyncConfig(
            SyncFrequency.MANUAL,
            null,  // Sin cron
            SyncStrategy.FULL_REFRESH,
            1,  // Prioridad (1 = mas alta)
            List.of(),  // Sin dependencias
            Map.of()
        ));

        // Leagues - Semanal los domingos
        entities.put("leagues", new EntitySyncConfig(
            SyncFrequency.WEEKLY,
            "0 0 0 * * SUN",
            SyncStrategy.UPSERT,
            2,
            List.of("countries"),
            Map.of("countryCode", "AR")
        ));

        // Teams - Semanal los lunes
        entities.put("teams", new EntitySyncConfig(
            SyncFrequency.WEEKLY,
            "0 0 0 * * MON",
            SyncStrategy.UPSERT,
            3,
            List.of("leagues"),
            Map.of("country", "Argentina")
        ));

        // Players - Semanal los martes
        entities.put("players", new EntitySyncConfig(
            SyncFrequency.WEEKLY,
            "0 0 0 * * TUE",
            SyncStrategy.UPSERT,
            4,
            List.of("teams"),
            Map.of("season", 2024)
        ));

        // Fixtures - Diario a las 6 AM
        entities.put("fixtures", new EntitySyncConfig(
            SyncFrequency.DAILY,
            "0 0 6 * * *",
            SyncStrategy.UPSERT,
            5,
            List.of("leagues"),
            Map.of("season", 2024)
        ));

        // Standings - Cada hora
        entities.put("standings", new EntitySyncConfig(
            SyncFrequency.HOURLY,
            "0 0 * * * *",
            SyncStrategy.FULL_REFRESH,
            6,
            List.of("fixtures"),
            Map.of("season", 2024)
        ));

        // FixtureStatistics - Disparado por evento
        entities.put("fixture_statistics", new EntitySyncConfig(
            SyncFrequency.EVENT_DRIVEN,
            null,
            SyncStrategy.UPSERT,
            7,
            List.of("fixtures"),
            Map.of()
        ));

        // FixtureEvents - Disparado por evento
        entities.put("fixture_events", new EntitySyncConfig(
            SyncFrequency.EVENT_DRIVEN,
            null,
            SyncStrategy.DELETE_INSERT,
            8,
            List.of("fixtures"),
            Map.of()
        ));

        // MatchLineups - Disparado por evento
        entities.put("match_lineups", new EntitySyncConfig(
            SyncFrequency.EVENT_DRIVEN,
            null,
            SyncStrategy.DELETE_INSERT,
            9,
            List.of("fixtures"),
            Map.of()
        ));
    }

    /**
     * Obtiene la configuracion para un tipo de entidad
     */
    public EntitySyncConfig getConfig(EntityType entityType) {
        return entities.get(entityType.getTableName());
    }

    /**
     * Obtiene la configuracion para un nombre de entidad
     */
    public EntitySyncConfig getConfig(String entityName) {
        return entities.get(entityName);
    }

    /**
     * Verifica si la sincronizacion esta habilitada globalmente
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getRateLimitDelay() {
        return rateLimitDelay;
    }

    public void setRateLimitDelay(Duration rateLimitDelay) {
        this.rateLimitDelay = rateLimitDelay;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Duration getStaleTimeout() {
        return staleTimeout;
    }

    public void setStaleTimeout(Duration staleTimeout) {
        this.staleTimeout = staleTimeout;
    }

    public Map<String, EntitySyncConfig> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, EntitySyncConfig> entities) {
        this.entities = entities;
    }

    /**
     * Configuracion de sincronizacion para una entidad especifica
     */
    public static class EntitySyncConfig {
        private SyncFrequency frequency;
        private String cron;
        private SyncStrategy strategy;
        private int priority;
        private List<String> dependencies;
        private Map<String, Object> parameters;

        public EntitySyncConfig() {
        }

        public EntitySyncConfig(
                SyncFrequency frequency,
                String cron,
                SyncStrategy strategy,
                int priority,
                List<String> dependencies,
                Map<String, Object> parameters) {
            this.frequency = frequency;
            this.cron = cron;
            this.strategy = strategy;
            this.priority = priority;
            this.dependencies = dependencies;
            this.parameters = parameters;
        }

        public SyncFrequency getFrequency() {
            return frequency;
        }

        public void setFrequency(SyncFrequency frequency) {
            this.frequency = frequency;
        }

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public SyncStrategy getStrategy() {
            return strategy;
        }

        public void setStrategy(SyncStrategy strategy) {
            this.strategy = strategy;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public List<String> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<String> dependencies) {
            this.dependencies = dependencies;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        /**
         * Verifica si la entidad requiere sincronizacion programada
         */
        public boolean isScheduled() {
            return cron != null && !cron.isBlank();
        }

        /**
         * Verifica si la entidad es disparada por eventos
         */
        public boolean isEventDriven() {
            return frequency == SyncFrequency.EVENT_DRIVEN;
        }
    }
}