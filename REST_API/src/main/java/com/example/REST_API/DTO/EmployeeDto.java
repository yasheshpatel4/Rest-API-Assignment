package com.example.REST_API.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeDto {

    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Email(message = "Invalid email")
    private String email;
    private String department;
    private String profilePicturePath;
}