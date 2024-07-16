package com.project.inventory_management.util;

import com.project.inventory_management.entity.User;
import com.project.inventory_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder= passwordEncoder;
    }

    // create a superuser at running time
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123123@"));
            admin.setEmail("admin@gmail.com");
            admin.setRole(User.Roles.ADMIN);
            admin.setCreatedAt(Timestamp.from(Instant.now()));
            admin.setUpdatedAt(Timestamp.from(Instant.now()));
            userRepository.save(admin);
        }
    }
}
