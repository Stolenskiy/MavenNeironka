package nic.com.Diplomka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image_db/**")
                .addResourceLocations("file:src/main/resources/image_db/");
        registry.addResourceHandler("/favorite_images/**")
                .addResourceLocations("file:src/main/resources/favorite_images/");
    }
}
