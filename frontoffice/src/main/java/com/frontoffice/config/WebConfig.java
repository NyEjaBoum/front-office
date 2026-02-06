package com.frontoffice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/", "classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("/js/", "classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("/images/", "classpath:/static/images/");
    }
}
