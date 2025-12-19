package com.iot.course.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticVideoConfig implements WebMvcConfigurer {
    @Value("${videos-static-path}")
    private String staticPath;
    @Value("${videos-path}")
    private String locationPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticPath + "/**")
                .addResourceLocations("file:" + locationPath + "/");
    }
}
