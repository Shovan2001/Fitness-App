package com.fitness.APIGateway.services.InterService.Impl;

import com.fitness.APIGateway.dto.UserRequestDTO;
import com.fitness.APIGateway.dto.UserResponseDTO;
import com.fitness.APIGateway.services.InterService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUserByUserId(String userId) {

        return userServiceWebClient.get()
                .uri("/api/users/userId/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                        return Mono.error(new RuntimeException("User not found: " + userId));

                    else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new RuntimeException("Invalid: " + userId));

                    return Mono.error(new RuntimeException("Unexpected Error from API Gateway !!"));
                });
    }

    public Boolean validateUserByKeyCloakId(String keyCloakId) {

        return userServiceWebClient.get()
                .uri("/api/users/keyCloakId/{keyCloakId}/validate", keyCloakId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    @Override
    public Mono<UserResponseDTO> registerUser(UserRequestDTO userRequestDTO) {

        log.info("Api Gateway USer Registering: {}", userRequestDTO.toString());

        return userServiceWebClient.post()
                .uri("api/users/register")
                .bodyValue(userRequestDTO)
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new RuntimeException("Bad Request!! " + e.getMessage()));

                    return Mono.error(new RuntimeException("Unexpected Error from API Gateway register User!! "
                            + e.getMessage()));
                });

    }


}
