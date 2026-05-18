package com.reservas.demo_reservas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigWebClient {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
