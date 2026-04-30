package com.sweetcorner.incubyte_backend.controller;

import com.sweetcorner.incubyte_backend.dto.LoginRequest;
import com.sweetcorner.incubyte_backend.dto.RegisterRequest;
import com.sweetcorner.incubyte_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.registerCustomer(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new com.sweetcorner.incubyte_backend.dto.ErrorResponse(e.getMessage()));
        }
    }
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
