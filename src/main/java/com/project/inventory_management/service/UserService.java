package com.project.inventory_management.service;

import com.project.inventory_management.config.JwtService;
import com.project.inventory_management.dto.AuthResponseDTO;
import com.project.inventory_management.dto.UserRequestDTO;
import com.project.inventory_management.dto.UserResponseDTO;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.exception.UserExistException;
import com.project.inventory_management.exception.UserNotFoundException;
import com.project.inventory_management.mapper.UserMapper;
import com.project.inventory_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO signup(UserRequestDTO.SignupRequestDTO signupRequestDTO) {
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
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return AuthResponseDTO
                .builder()
                .token(token)
                .build();
    }

    public AuthResponseDTO login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        User user = userRepository.findByEmail(email)
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO getUserById(int id) {
        return userMapper.toUserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO updateUser(
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
            return userMapper.toUserResponseDTO(userRepository.save(user));
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
