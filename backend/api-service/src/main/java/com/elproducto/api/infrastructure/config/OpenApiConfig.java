package com.elproducto.api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public OpenAPI elProductoOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local development server");

        Server prodServer = new Server();
        prodServer.setUrl(frontendUrl);
        prodServer.setDescription("Production server");

        Contact contact = new Contact();
        contact.setName("ElProducto Team");
        contact.setEmail("contact@elproducto.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("ElProducto API")
                .version("1.0.0")
                .description("REST API for sports results and statistics")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, prodServer));
    }
}
