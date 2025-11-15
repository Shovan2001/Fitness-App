package com.fitness.ActivityService.services.InterService.Impl;

import com.fitness.ActivityService.services.InterService.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final WebClient userServiceWebClient;

    public Boolean validateUserByUserId(String userId) {

        return userServiceWebClient.get()
                .uri("/api/users/userId/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public Boolean validateUserByKeyCloakId(String keyCloakId) {

        return userServiceWebClient.get()
                .uri("/api/users/keyCloakId/{keyCloakId}/validate", keyCloakId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}
