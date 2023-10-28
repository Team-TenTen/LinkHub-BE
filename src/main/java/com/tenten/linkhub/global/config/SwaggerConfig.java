package com.tenten.linkhub.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.NoArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "LinkHub App",
                description = "LinkHub app api명세",
                version = "v1"))
@NoArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("LinkHub 서비스 API v1")
                .pathsToMatch(paths)
                .build();
    }

}
