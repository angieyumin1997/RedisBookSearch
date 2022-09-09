package vttp.csf.day34.server.configurations;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CORSConfigurations implements WebMvcConfigurer{

    private String path;
    private String origins;

    public CORSConfigurations(String p, String o) {
        path = p; // /api/* 
        origins = o; //* 
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry
            .addMapping(path)
            .allowedOrigins(origins);
            // .allowedHeaders("*")
            // .allowCredentials(true);

    }
}
