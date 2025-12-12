package com.elproducto.datacollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del microservicio Data Collector
 * Recolecta datos de API-Football y los almacena localmente
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.elproducto.datacollector")
public class DataCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCollectorApplication.class, args);
    }
}