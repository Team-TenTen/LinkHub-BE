package com.tenten.linkhub.global.config;

import com.tenten.linkhub.domain.space.SpaceViewCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpaceViewCheckInterceptor(applicationEventPublisher))
                .addPathPatterns("/spaces/**");
    }

}
