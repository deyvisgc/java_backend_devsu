package com.example.prueba_tecnica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;

import java.util.Collections;

@Configuration
@EnableSwagger2

public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.prueba_tecnica.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                ;
    }
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Api Servicios Bancarios Devsu",
                "Transacciones bancarias",
                "1.0",
                "http://codmind.com/terms",
                new Contact("Deyvis Garcia", "https://www.linkedin.com/in/deyvis-ronald-garcia-cercado-a503ba163/", "deyvisgarcia049@gmail.com"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );
    }
}
