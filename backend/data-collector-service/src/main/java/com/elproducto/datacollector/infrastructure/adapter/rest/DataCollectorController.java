package com.elproducto.datacollector.infrastructure.adapter.rest;

import com.elproducto.datacollector.application.usecase.CollectCountriesUseCase;
import com.elproducto.datacollector.domain.model.Country;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para ejecutar procesos de recolección de datos
 * Expone endpoints para ejecutar el proceso bajo demanda
 */
@RestController
@RequestMapping("/api/v1/collector")
@Tag(name = "Data Collector", description = "Endpoints para recolección de datos desde API-Football")
public class DataCollectorController {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorController.class);

    private final CollectCountriesUseCase collectCountriesUseCase;

    public DataCollectorController(CollectCountriesUseCase collectCountriesUseCase) {
        this.collectCountriesUseCase = collectCountriesUseCase;
    }

    /**
     * Ejecuta el proceso de recolección de países bajo demanda
     *
     * @return Lista de países recolectados
     */
    @Operation(
        summary = "Recolectar países",
        description = "Ejecuta el proceso de recolección de países desde API-Football. " +
                      "Este endpoint consulta la API externa y devuelve todos los países disponibles " +
                      "con su información (nombre, código ISO y bandera)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Países recolectados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CollectionResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "status": "SUCCESS",
                          "message": "Países recolectados exitosamente",
                          "count": 165,
                          "data": [
                            {
                              "name": "Argentina",
                              "code": "AR",
                              "flag": "https://media.api-sports.io/flags/ar.svg"
                            },
                            {
                              "name": "Brazil",
                              "code": "BR",
                              "flag": "https://media.api-sports.io/flags/br.svg"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CollectionResponse.class),
                examples = @ExampleObject(
                    name = "Error de servidor",
                    value = """
                        {
                          "status": "ERROR",
                          "message": "Error: Connection timeout",
                          "count": 0,
                          "data": []
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/countries")
    public ResponseEntity<CollectionResponse> collectCountries() {
        logger.info("🚀 Endpoint /api/v1/collector/countries invocado - Iniciando proceso de recolección");

        try {
            List<Country> countries = collectCountriesUseCase.execute();

            CollectionResponse response = new CollectionResponse(
                "SUCCESS",
                "Países recolectados exitosamente",
                countries.size(),
                countries
            );

            logger.info("✅ Proceso completado exitosamente - {} países recolectados", countries.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error ejecutando proceso de recolección", e);

            CollectionResponse errorResponse = new CollectionResponse(
                "ERROR",
                "Error: " + e.getMessage(),
                0,
                List.of()
            );

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * DTO para la respuesta del endpoint
     */
    public record CollectionResponse(
        String status,
        String message,
        int count,
        List<Country> data
    ) {}
}