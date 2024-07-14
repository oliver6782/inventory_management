package com.project.inventorymanagement.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.project.inventorymanagement.config.RoleEnumDeserializer;
import com.project.inventorymanagement.entity.User;
import com.project.inventorymanagement.validation.ValidEnum;
import com.project.inventorymanagement.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserRequestDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupRequestDTO {

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be at least 4 characters and less than 50 characters")
        private String username;

        @NotBlank(message = "Password is required")
        @ValidPassword
        private String password;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequestDTO {

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUserRequestDTO {

        @Size(min = 4, max = 50, message = "Username must be at least 4 characters and less than 50 characters")
        private String username;

        @ValidPassword
        private String password;

        @Email(message = "Email should be valid")
        private String email;

        @NotNull(message = "Role can not be null")
        @ValidEnum(enumClass = User.Roles.class)
        @JsonDeserialize(using = RoleEnumDeserializer.class)
        private User.Roles role;
    }

}

