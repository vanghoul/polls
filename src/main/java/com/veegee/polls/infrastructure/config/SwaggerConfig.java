package com.veegee.polls.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SwaggerConfig {

    private static final String CONTROLLERS_PACKAGE = "com.veegee.polls.api.controller";
    private static final String APIS_BASE_PATH = "/api/**";

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                    .select()
                    .apis(basePackage(CONTROLLERS_PACKAGE))
                    .paths(ant(APIS_BASE_PATH))
                .build();
    }
}
