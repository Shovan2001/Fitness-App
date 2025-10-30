package com.fitness.AIService.services.InterService.Impl;

import com.fitness.AIService.services.InterService.ActivityValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ActivityValidationServiceImpl implements ActivityValidationService {

    private final WebClient activityServiceWebClient;

    @Override
    public Boolean validateActivityId(String activityId) {

        return activityServiceWebClient
                .get()
                .uri("/api/activities/{activityId}/validate",activityId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
