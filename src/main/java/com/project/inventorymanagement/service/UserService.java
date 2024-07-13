package com.project.inventorymanagement.service;

import com.project.inventorymanagement.dto.SignupRequest;
import com.project.inventorymanagement.entity.User;
import com.project.inventorymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(SignupRequest signupRequest) {
        User user = new User();
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(signupRequest.getEmail());
        user.setRole(User.Roles.USER);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        user.setUpdatedAt(Timestamp.from(Instant.now()));
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Incorrect password");
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteUserById(int id) {
        userRepository.deleteById(id);
        return true;
    }
}
