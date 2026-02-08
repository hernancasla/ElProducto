package com.elproducto.datacollector.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuracion para habilitar scheduling en Spring
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    // La anotacion @EnableScheduling habilita el procesamiento de @Scheduled
}
