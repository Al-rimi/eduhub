package com.syalux.eduhub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        // Forward all routes except those containing a dot (.) to index.html
        registry.addViewController("/{spring:[^\\.]*}").setViewName("forward:/index.html");
    }
}
