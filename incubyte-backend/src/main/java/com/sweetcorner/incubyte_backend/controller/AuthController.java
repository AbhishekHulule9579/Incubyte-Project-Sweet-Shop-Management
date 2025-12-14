package com.sweetcorner.incubyte_backend.controller;

import com.sweetcorner.incubyte_backend.dto.LoginRequest;
import com.sweetcorner.incubyte_backend.dto.RegisterRequest;
import com.sweetcorner.incubyte_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related requests.
 * Manages user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor for dependency injection.
     *
     * @param authService Service for handling authentication logic.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for registering a new customer.
     *
     * @param request The registration request payload containing user details.
     * @return ResponseEntity containing the registration result or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.registerCustomer(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new com.sweetcorner.incubyte_backend.dto.ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint for user login.
     * Authenticates the user and returns a JWT token.
     *
     * @param request The login request payload containing credentials.
     * @return ResponseEntity containing the authentication token or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                    .body(new com.sweetcorner.incubyte_backend.dto.ErrorResponse(e.getMessage()));
        }
    }
}
