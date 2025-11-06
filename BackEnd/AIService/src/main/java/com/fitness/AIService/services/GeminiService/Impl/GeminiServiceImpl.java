package com.fitness.AIService.services.GeminiService.Impl;

import com.fitness.AIService.models.GeminiRequestBody;
import com.fitness.AIService.services.GeminiService.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService {

    private final WebClient geminiServiceWebClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.apikey}")
    private String geminiApiKey;

    public String getGeminiResponse(GeminiRequestBody geminiRequestBody)
    {
        log.info("Inside GeminiServiceImpl getGeminiResponse");

        return geminiServiceWebClient
                .post()
                .uri(geminiApiUrl)
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(geminiRequestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .block();
    }
}
