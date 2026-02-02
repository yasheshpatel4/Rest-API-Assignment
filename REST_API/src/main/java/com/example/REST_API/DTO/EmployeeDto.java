package com.example.REST_API.DTO;

import com.example.REST_API.validate.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class EmployeeDto {

    private Long id;
    @NotBlank(message = "Name is mandatory",groups = OnUpdate.class)
    private String name;
    @Email(message = "Invalid email")
    private String email;
    private String department;
//    private String profilePicturePath;
}