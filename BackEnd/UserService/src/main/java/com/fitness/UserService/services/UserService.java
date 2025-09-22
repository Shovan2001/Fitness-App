package com.fitness.UserService.services;

import com.fitness.UserService.dto.UserRequestDTO;
import com.fitness.UserService.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO register(UserRequestDTO request);

    UserResponseDTO getUserProfile(String userId);

    List<UserResponseDTO> getAllUserProfiles();
}
