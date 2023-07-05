package com.michalharasim.githublisting;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    Gson getGson() {
        return new Gson();
    }
}
