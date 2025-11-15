package com.fitness.ActivityService.services.InterService;

public interface UserValidationService {

    Boolean validateUserByUserId(String userId);

    Boolean validateUserByKeyCloakId(String keyCloakId);

}
