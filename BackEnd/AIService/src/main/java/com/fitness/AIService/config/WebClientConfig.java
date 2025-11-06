package com.fitness.AIService.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://USERSERVICE").build();
    }

    @Bean
    public WebClient activityServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://ACTIVITYSERVICE").build();
    }

    // for external api hits (Gemini)
    // Cant use load Balanced, or it will give error.
    // Load balanced is used for Microservices not for 3rd part api
    @Bean
    public WebClient geminiServiceWebClient()
    {
        return  WebClient.builder().build();
    }
}
