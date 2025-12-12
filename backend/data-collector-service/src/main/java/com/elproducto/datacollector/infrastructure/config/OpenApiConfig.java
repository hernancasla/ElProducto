package com.elproducto.datacollector.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private int serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Data Collector Service API")
                .version("0.0.1-SNAPSHOT")
                .description("""
                    # Microservicio de Recolección de Datos

                    Este servicio se encarga de recolectar datos de API-Football y almacenarlos
                    en el repositorio local para su posterior procesamiento.

                    ## Características
                    - ✅ Recolección de datos de países desde API-Football
                    - ✅ Arquitectura hexagonal
                    - ✅ WebFlux reactivo
                    - ✅ Health checks y métricas

                    ## Próximas funcionalidades
                    - ⏳ Recolección de ligas
                    - ⏳ Recolección de equipos
                    - ⏳ Recolección de jugadores
                    - ⏳ Scheduler para ejecución automática
                    """)
                .contact(new Contact()
                    .name("ElProducto Team")
                    .email("support@elproducto.com")
                    .url("https://elproducto.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Development server"),
                new Server()
                    .url("http://localhost:8081")
                    .description("Default local server")
            ));
    }
}