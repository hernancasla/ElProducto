package com.elproducto.api.config;

import com.elproducto.api.security.ApiKeyAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.admin.api-key}")
    private String adminApiKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // REST API — no CSRF needed
            .csrf(AbstractHttpConfigurer::disable)

            // No HTTP sessions — each request is authenticated by API key
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Disable default form login and basic auth pop-up
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                // Public: all sports data read endpoints
                .requestMatchers(HttpMethod.GET, "/api/v1/matches/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/teams/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/leagues/**").permitAll()

                // Public: Swagger UI and OpenAPI docs (dev only — disabled in prod profile)
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()

                // Public: basic health check
                .requestMatchers("/actuator/health").permitAll()

                // Protected: all admin endpoints and Flyway actuator
                .requestMatchers("/api/v1/admin/**").authenticated()
                .requestMatchers("/actuator/flyway").authenticated()
                .requestMatchers("/actuator/**").authenticated()

                // Default: deny anything not explicitly allowed
                .anyRequest().authenticated()
            )

            // Plug in the API key filter before Spring's username/password filter
            .addFilterBefore(new ApiKeyAuthFilter(adminApiKey),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
