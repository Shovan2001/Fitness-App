package com.fitness.AIService.services.InterService;

public interface UserValidationService {

    Boolean validateUserByUserId(String userId);

    Boolean validateUserByKeyCloakId(String keyCloakId);

}
