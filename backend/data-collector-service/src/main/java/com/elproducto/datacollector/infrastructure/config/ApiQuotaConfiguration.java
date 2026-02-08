package com.elproducto.datacollector.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de quota para API-Football
 * Controla el limite de llamadas diarias
 */
@Configuration
@ConfigurationProperties(prefix = "api-football.quota")
public class ApiQuotaConfiguration {

    /**
     * Habilita/deshabilita el control de quota
     */
    private boolean enabled = true;

    /**
     * Limite diario de llamadas a la API
     * Free plan: 100/day
     * Pro plan: 7,500/day
     */
    private int dailyLimit = 100;

    /**
     * Porcentaje de alerta (ej: 80 = alerta cuando se usa 80% del limite)
     */
    private int warningThresholdPercent = 80;

    /**
     * Si true, bloquea las llamadas cuando se alcanza el limite
     * Si false, solo loguea warning pero permite continuar
     */
    private boolean blockOnLimit = true;

    /**
     * Hora del dia (0-23) cuando se resetea el contador
     * API-Football resetea a las 00:00 UTC
     */
    private int resetHourUtc = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(int dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public int getWarningThresholdPercent() {
        return warningThresholdPercent;
    }

    public void setWarningThresholdPercent(int warningThresholdPercent) {
        this.warningThresholdPercent = warningThresholdPercent;
    }

    public boolean isBlockOnLimit() {
        return blockOnLimit;
    }

    public void setBlockOnLimit(boolean blockOnLimit) {
        this.blockOnLimit = blockOnLimit;
    }

    public int getResetHourUtc() {
        return resetHourUtc;
    }

    public void setResetHourUtc(int resetHourUtc) {
        this.resetHourUtc = resetHourUtc;
    }

    /**
     * Calcula el numero de llamadas que dispara el warning
     */
    public int getWarningThreshold() {
        return (dailyLimit * warningThresholdPercent) / 100;
    }

    /**
     * Calcula llamadas restantes dado un contador actual
     */
    public int getRemainingCalls(int currentCount) {
        return Math.max(0, dailyLimit - currentCount);
    }

    /**
     * Verifica si se alcanzo el limite
     */
    public boolean isLimitReached(int currentCount) {
        return currentCount >= dailyLimit;
    }

    /**
     * Verifica si se alcanzo el umbral de warning
     */
    public boolean isWarningThresholdReached(int currentCount) {
        return currentCount >= getWarningThreshold();
    }
}