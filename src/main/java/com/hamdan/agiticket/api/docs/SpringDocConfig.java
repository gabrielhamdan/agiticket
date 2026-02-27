package com.hamdan.agiticket.api.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("AgiTicket REST API")
                        .description("API REST para gerenciamento de chamados com controle de acesso baseado em permissão de usuário")
                        .version("1.0").contact(new Contact().name("Gabriel Hamdan")
                                .email( "gabriel_hamdan@hotmail.com").url("https://www.linkedin.com/in/gabriel-hamdan/"))
                        .license(new License().name("MIT License")
                                .url("https://mit-license.org/")));
    }

}
