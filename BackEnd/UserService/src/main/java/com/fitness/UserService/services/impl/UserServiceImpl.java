package com.fitness.UserService.services.impl;

import com.fitness.UserService.dto.UserRequestDTO;
import com.fitness.UserService.dto.UserResponseDTO;
import com.fitness.UserService.models.Users;
import com.fitness.UserService.repository.UserRepository;
import com.fitness.UserService.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserResponseDTO register(UserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("User with this email already exists");

        Users user = new Users();

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        Users savedUser = userRepository.save(user);

        return getUserResponseDTOFromUser(savedUser);
    }

    @Override
    public UserResponseDTO getUserProfile(String userId) {

        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return getUserResponseDTOFromUser(user);
    }

    @Override
    public List<UserResponseDTO> getAllUserProfiles() {

        // NO STREAMS
//        List<Users> users = userRepository.findAll();
//
//        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
//
//        for (Users user : users) {
//            userResponseDTOS.add(getUserResponseDTOFromUser(user));
//        }
//        return userResponseDTOS;

        // USING STREAMS
        List<UserResponseDTO> userResponseDTOS;

        userResponseDTOS = userRepository.findAll()
                .stream()
                .map(this::getUserResponseDTOFromUser)
                .collect(Collectors.toList());

        return userResponseDTOS;
    }

    @Override
    public Boolean validateUserId(String userId) {

        return userRepository.existsById(userId);
    }

    private UserResponseDTO getUserResponseDTOFromUser(Users user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setUpdatedAt(user.getUpdatedAt());

        return userResponseDTO;
    }
}
