package com.fitness.APIGateway.services.InterService;

import com.fitness.APIGateway.dto.UserRequestDTO;
import com.fitness.APIGateway.dto.UserResponseDTO;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Boolean> validateUserByUserId(String userId);

    Boolean validateUserByKeyCloakId(String keyCloakId);

    Mono<UserResponseDTO> registerUser(UserRequestDTO userRequestDTO);
}
