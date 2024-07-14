package com.project.inventorymanagement.controller;

import com.project.inventorymanagement.dto.UserRequestDTO;
import com.project.inventorymanagement.entity.User;
import com.project.inventorymanagement.service.UserService;
import com.project.inventorymanagement.util.ValidationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserController(UserService userService, ValidationUtil validationUtil) {
        this.userService = userService;
        this.validationUtil = validationUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserRequestDTO.SignupRequestDTO signupRequestDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.generateValidationErrorResponse(bindingResult);
        }
        return ResponseEntity.ok(userService.signup(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequestDTO.LoginRequestDTO loginRequestDTO,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationUtil.generateValidationErrorResponse(bindingResult);
        }
        return ResponseEntity.ok(
                userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody UserRequestDTO.UpdateUserRequestDTO updateUserRequestDTO,
            BindingResult bindingResult,
            @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            return validationUtil.generateValidationErrorResponse(bindingResult);
        }
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

}
