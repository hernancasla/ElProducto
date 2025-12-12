package com.elproducto.datacollector.infrastructure.adapter.http;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.domain.port.FootballApiClient;
import com.elproducto.datacollector.infrastructure.adapter.http.dto.ApiFootballResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de infraestructura para consumir API-Football
 * Implementa el puerto FootballApiClient usando WebClient
 */
@Component
public class ApiFootballClient implements FootballApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiFootballClient.class);

    private final WebClient webClient;

    public ApiFootballClient(
            @Value("${api-football.base-url}") String baseUrl,
            @Value("${api-football.rapidapi-host}") String rapidApiHost,
            @Value("${api-football.rapidapi-key}") String rapidApiKey,
            @Value("${api-football.timeout}") int timeout) {

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-rapidapi-host", rapidApiHost)
                .defaultHeader("x-rapidapi-key", rapidApiKey)
                .build();

        logger.info("ApiFootballClient initialized with base URL: {}", baseUrl);
    }

    @Override
    public List<Country> fetchCountries() {
        logger.info("Fetching countries from API-Football endpoint: /teams/countries");

        try {
            ApiFootballResponse response = webClient.get()
                    .uri("/teams/countries")
                    .retrieve()
                    .bodyToMono(ApiFootballResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || response.response() == null) {
                logger.warn("Empty response received from API");
                return List.of();
            }

            logger.info("API Response received - Total countries: {}", response.response().size());
            logger.debug("Full API Response: {}", response);

            // Convertir DTOs a modelos de dominio
            List<Country> countries = response.response().stream()
                    .map(dto -> new Country(
                            dto.name(),
                            dto.code(),
                            dto.flag()
                    ))
                    .collect(Collectors.toList());

            logger.info("Converted {} countries to domain models", countries.size());
            return countries;

        } catch (Exception e) {
            logger.error("Error fetching countries from API-Football", e);
            throw new RuntimeException("Failed to fetch countries from external API", e);
        }
    }
}