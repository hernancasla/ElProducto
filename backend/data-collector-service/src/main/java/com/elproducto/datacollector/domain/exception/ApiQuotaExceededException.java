package com.elproducto.datacollector.domain.exception;

/**
 * Excepcion lanzada cuando se excede la quota de llamadas a la API
 */
public class ApiQuotaExceededException extends RuntimeException {

    private final int currentCount;
    private final int limit;

    public ApiQuotaExceededException(int currentCount, int limit) {
        super(String.format("API quota exceeded: %d/%d daily calls used", currentCount, limit));
        this.currentCount = currentCount;
        this.limit = limit;
    }

    public ApiQuotaExceededException(String message) {
        super(message);
        this.currentCount = 0;
        this.limit = 0;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getLimit() {
        return limit;
    }

    public int getRemaining() {
        return Math.max(0, limit - currentCount);
    }
}