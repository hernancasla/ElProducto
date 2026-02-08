package com.elproducto.datacollector.application.service;

import com.elproducto.datacollector.infrastructure.config.ApiQuotaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Servicio para controlar la quota de llamadas a API-Football
 * Mantiene un contador diario y bloquea llamadas si se excede el limite
 */
@Service
public class ApiQuotaService {

    private static final Logger logger = LoggerFactory.getLogger(ApiQuotaService.class);

    private final ApiQuotaConfiguration config;

    // Contador de llamadas del dia actual
    private final AtomicInteger dailyCallCount = new AtomicInteger(0);

    // Fecha del contador actual (para reset diario)
    private final AtomicReference<LocalDate> counterDate = new AtomicReference<>(LocalDate.now(ZoneOffset.UTC));

    // Estadisticas adicionales
    private final AtomicInteger totalCallsBlocked = new AtomicInteger(0);
    private final AtomicInteger totalCallsAllTime = new AtomicInteger(0);

    public ApiQuotaService(ApiQuotaConfiguration config) {
        this.config = config;
        logger.info("[QUOTA] Servicio de quota inicializado - Limite diario: {}, Bloqueo activo: {}",
            config.getDailyLimit(), config.isBlockOnLimit());
    }

    /**
     * Verifica si se puede hacer una llamada a la API
     * @return Mono<Boolean> true si se permite, false si no
     */
    public Mono<Boolean> canMakeCall() {
        if (!config.isEnabled()) {
            return Mono.just(true);
        }

        checkAndResetIfNewDay();

        int currentCount = dailyCallCount.get();

        if (config.isLimitReached(currentCount)) {
            if (config.isBlockOnLimit()) {
                logger.warn("[QUOTA] LIMITE ALCANZADO - {}/{} llamadas. Bloqueando peticion.",
                    currentCount, config.getDailyLimit());
                totalCallsBlocked.incrementAndGet();
                return Mono.just(false);
            } else {
                logger.warn("[QUOTA] LIMITE EXCEDIDO - {}/{} llamadas. Continuando (blockOnLimit=false)",
                    currentCount, config.getDailyLimit());
            }
        }

        return Mono.just(true);
    }

    /**
     * Registra una llamada realizada a la API
     * Debe llamarse despues de cada llamada exitosa
     */
    public void recordCall() {
        if (!config.isEnabled()) {
            return;
        }

        checkAndResetIfNewDay();

        int newCount = dailyCallCount.incrementAndGet();
        totalCallsAllTime.incrementAndGet();

        // Log de progreso
        if (newCount % 10 == 0 || config.isWarningThresholdReached(newCount)) {
            int remaining = config.getRemainingCalls(newCount);
            int percentUsed = (newCount * 100) / config.getDailyLimit();

            if (config.isWarningThresholdReached(newCount)) {
                logger.warn("[QUOTA] {} ALERTA: {}/{} llamadas usadas ({}%). Restantes: {}",
                    getWarningEmoji(percentUsed), newCount, config.getDailyLimit(), percentUsed, remaining);
            } else {
                logger.info("[QUOTA] Llamadas hoy: {}/{} ({}%). Restantes: {}",
                    newCount, config.getDailyLimit(), percentUsed, remaining);
            }
        }
    }

    /**
     * Registra una llamada y retorna el resultado
     * Util para encadenar en flujos reactivos
     */
    public <T> Mono<T> recordCallAndReturn(T result) {
        recordCall();
        return Mono.just(result);
    }

    /**
     * Verifica si es un nuevo dia y resetea el contador
     */
    private void checkAndResetIfNewDay() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate lastDate = counterDate.get();

        if (!today.equals(lastDate)) {
            if (counterDate.compareAndSet(lastDate, today)) {
                int previousCount = dailyCallCount.getAndSet(0);
                logger.info("[QUOTA] Nuevo dia detectado. Contador reseteado. Llamadas del dia anterior: {}",
                    previousCount);
            }
        }
    }

    /**
     * Obtiene el estado actual de la quota
     */
    public QuotaStatus getStatus() {
        checkAndResetIfNewDay();

        int currentCount = dailyCallCount.get();
        int limit = config.getDailyLimit();
        int remaining = config.getRemainingCalls(currentCount);
        int percentUsed = limit > 0 ? (currentCount * 100) / limit : 0;

        return new QuotaStatus(
            config.isEnabled(),
            currentCount,
            limit,
            remaining,
            percentUsed,
            config.isLimitReached(currentCount),
            config.isWarningThresholdReached(currentCount),
            config.isBlockOnLimit(),
            counterDate.get(),
            totalCallsBlocked.get(),
            totalCallsAllTime.get()
        );
    }

    /**
     * Resetea el contador manualmente (para testing o admin)
     */
    public void resetCounter() {
        int previous = dailyCallCount.getAndSet(0);
        counterDate.set(LocalDate.now(ZoneOffset.UTC));
        logger.info("[QUOTA] Contador reseteado manualmente. Valor anterior: {}", previous);
    }

    /**
     * Ajusta el contador a un valor especifico (para sincronizar con API response headers)
     */
    public void setCounter(int value) {
        dailyCallCount.set(value);
        logger.info("[QUOTA] Contador ajustado a: {}", value);
    }

    private String getWarningEmoji(int percentUsed) {
        if (percentUsed >= 95) return "🔴";
        if (percentUsed >= 90) return "🟠";
        if (percentUsed >= 80) return "🟡";
        return "⚠️";
    }

    /**
     * Estado de la quota
     */
    public record QuotaStatus(
        boolean enabled,
        int callsToday,
        int dailyLimit,
        int remaining,
        int percentUsed,
        boolean limitReached,
        boolean warningReached,
        boolean blockOnLimit,
        LocalDate counterDate,
        int totalCallsBlocked,
        int totalCallsAllTime
    ) {
        public String getStatusMessage() {
            if (!enabled) return "Quota control disabled";
            if (limitReached) return "LIMIT REACHED - Calls blocked";
            if (warningReached) return "WARNING - Approaching limit";
            return "OK";
        }
    }
}