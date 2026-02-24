package com.blogapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI blogOpenAPI() {
        // Deployed Server Configuration
        Server deployedServer = new Server()
                .url("http://93.127.194.118:8024")
                .description("Deployed Server");

        // Local Development Configuration
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Development");

        return new OpenAPI()
                .info(new Info()
                        .title("Blog Application API")
                        .description("REST API documentation for the Blog Module — supports blog listing, "
                                + "submission with email OTP verification, reactions, comments, "
                                + "subscriptions, and admin moderation.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Blog Admin")
                                .email("sales.webarya@gmail.com")))
                // The first server in this list becomes the default in Swagger UI
                .servers(List.of(deployedServer, localServer));
    }
}
