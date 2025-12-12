package com.elproducto.datacollector.infrastructure.config;

import com.elproducto.datacollector.domain.model.Country;
import com.elproducto.datacollector.infrastructure.adapter.http.dto.ApiFootballResponse;
import com.elproducto.datacollector.infrastructure.adapter.http.dto.CountryDto;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de hints para GraalVM Native Image
 * Registra clases que requieren reflection para serialización/deserialización
 */
@Configuration
@RegisterReflectionForBinding({
    Country.class,
    CountryDto.class,
    ApiFootballResponse.class
})
public class GraalVmConfig {
    // Los hints se configuran mediante la anotación @RegisterReflectionForBinding
    // Spring Boot 3.2+ maneja automáticamente la mayoría de casos
}