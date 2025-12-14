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

/**
 * Service class for handling authentication logic.
 * Manages user registration and login processes.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository  Repository for User data.
     * @param adminRepository Repository for Admin data.
     * @param passwordEncoder utility for password encoding.
     * @param jwtUtils        Utility for JWT operations.
     */
    public AuthService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Registers a new customer in the system.
     *
     * @param request The registration request containing user details.
     * @return An AuthResponse containing the JWT token and user info.
     * @throws RuntimeException if the email already exists.
     */
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

    /**
     * Authenticates a user (Admin or Customer) and generates a JWT token.
     *
     * @param request The login request containing credentials.
     * @return An AuthResponse containing the JWT token and user info.
     * @throws RuntimeException if the user is not found or credentials are invalid.
     */
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
