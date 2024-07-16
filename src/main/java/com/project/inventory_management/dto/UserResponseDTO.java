package com.project.inventory_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

// declare a DTO to specify instances that users can get by calling request
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
