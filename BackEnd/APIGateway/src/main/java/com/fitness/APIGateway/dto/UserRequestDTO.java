package com.fitness.APIGateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    private String keyCloakId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Email is required")
    @Size(message = "Must be atleast 8 characters")
    private String password;
    private String firstName;
    private String lastName;

}
