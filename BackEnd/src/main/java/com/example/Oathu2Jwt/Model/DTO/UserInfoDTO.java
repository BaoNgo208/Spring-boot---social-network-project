package com.example.Oathu2Jwt.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private Long id;

    @NotEmpty(message = "User email must not be empty") //Neither null nor 0 size
    @Email(message = "Invalid email format")
    private String emailId;
    @NotEmpty(message = "User password must not be empty")
    private String password;

    @NotEmpty(message = "User role must not be empty")
    private String roles;
    @NotEmpty(message = "acc name must not be empty")
    private String accName;

    private EmployeeDTO employee;

}
