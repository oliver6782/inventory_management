package com.project.inventory_management.controller;

import com.project.inventory_management.dto.UserRequestDTO;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.exception.UserNotFoundException;
import com.project.inventory_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserRequestDTO.SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(userService.signup(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserRequestDTO.LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(
                userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody UserRequestDTO.UpdateUserRequestDTO updateUserRequestDTO,
            @PathVariable Integer id) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequestDTO, id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @GetMapping("/user-not-found")
    public UserNotFoundException userNotFound() {
        return new UserNotFoundException("User not found");
    }

}
