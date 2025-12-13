package com.sweetcorner.incubyte_backend.service;

import com.sweetcorner.incubyte_backend.dto.AuthResponse;
import com.sweetcorner.incubyte_backend.dto.LoginRequest;
import com.sweetcorner.incubyte_backend.dto.RegisterRequest;
import com.sweetcorner.incubyte_backend.entity.Admin;
import com.sweetcorner.incubyte_backend.entity.User;
import com.sweetcorner.incubyte_backend.repository.AdminRepository;
import com.sweetcorner.incubyte_backend.repository.UserRepository;
import com.sweetcorner.incubyte_backend.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse registerCustomer(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER");

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail(), "CUSTOMER");
        return new AuthResponse(token, user.getEmail(), user.getFullName(), "CUSTOMER");
    }

    public AuthResponse login(LoginRequest request) {
        String role = request.getRole() != null ? request.getRole().toUpperCase() : "CUSTOMER";

        if ("ADMIN".equals(role)) {
            Admin admin = adminRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            // Direct comparison for Admin (since manual entry was plain text)
            if (!request.getPassword().equals(admin.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            String token = jwtUtils.generateToken(admin.getEmail(), "ADMIN");
            return new AuthResponse(token, admin.getEmail(), admin.getFullName(), "ADMIN");
        } else {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            String token = jwtUtils.generateToken(user.getEmail(), "CUSTOMER");
            return new AuthResponse(token, user.getEmail(), user.getFullName(), "CUSTOMER");
        }
    }
}
