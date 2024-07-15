package com.project.inventory_management.service;

import com.project.inventory_management.dto.UserRequestDTO;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.exception.IncorrectPasswordException;
import com.project.inventory_management.exception.UserExistException;
import com.project.inventory_management.exception.UserNotFoundException;
import com.project.inventory_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(UserRequestDTO.SignupRequestDTO signupRequestDTO) {
        String signupEmail = signupRequestDTO.getEmail();
        Optional<User> existUser = userRepository.findByEmail(signupEmail);
        if (existUser.isPresent()) {
            throw new UserExistException("User already exists with email: " + signupEmail);
        }
        User user = new User();
        String encodedPassword = passwordEncoder.encode(signupRequestDTO.getPassword());
        user.setUsername(signupRequestDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(signupEmail);
        user.setRole(User.Roles.USER);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        user.setUpdatedAt(Timestamp.from(Instant.now()));
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new IncorrectPasswordException("Incorrect password");
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(
            UserRequestDTO.UpdateUserRequestDTO updateUserRequestDTO,
            Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (updateUserRequestDTO.getUsername() != null) {
                user.setUsername(updateUserRequestDTO.getUsername());
            }
            if (updateUserRequestDTO.getEmail() != null) {
                user.setEmail(updateUserRequestDTO.getEmail());
            }
            if (updateUserRequestDTO.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(updateUserRequestDTO.getPassword());
                user.setPassword(encodedPassword);
            }
            user.setUpdatedAt(Timestamp.from(Instant.now()));
            return userRepository.save(user);
        }
        throw new UserNotFoundException("User not found");
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteUserById(int id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(userToDelete);
        return true;
    }
}
