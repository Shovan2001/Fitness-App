package com.fitness.AIService.services.InterService.Impl;

import com.fitness.AIService.services.InterService.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final WebClient userServiceWebClient;

    public Boolean validateUserId(String userId) {

        return userServiceWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}
