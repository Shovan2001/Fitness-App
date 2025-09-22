package com.fitness.UserService.controller;

import com.fitness.UserService.dto.UserRequestDTO;
import com.fitness.UserService.dto.UserResponseDTO;
import com.fitness.UserService.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable String userId) {

        UserResponseDTO userResponseDTO = userService.getUserProfile(userId);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {

        List<UserResponseDTO> userProfiles = userService.getAllUserProfiles();

        return new ResponseEntity<>(userProfiles,HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO request) {

        UserResponseDTO userResponseDTO = userService.register(request);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

}
