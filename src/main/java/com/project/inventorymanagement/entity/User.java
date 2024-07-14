package com.project.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public enum Roles {USER, ADMIN}

    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    private Roles role;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

}
