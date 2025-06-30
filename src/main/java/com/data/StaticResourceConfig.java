package com.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Value;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
//    @Value("${file.upload-dir}")
//    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Assuming your images are saved in folder named 'uploads' at your project root or server path
        registry.addResourceHandler("/uploaded-images/**")
                .addResourceLocations("file:uploaded-images/");
    }
}

