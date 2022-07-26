package com.hotels.config;

import com.hotels.Main;
import com.hotels.assignment.Assignment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Main myMain(){
        return new Main();
    }
}
