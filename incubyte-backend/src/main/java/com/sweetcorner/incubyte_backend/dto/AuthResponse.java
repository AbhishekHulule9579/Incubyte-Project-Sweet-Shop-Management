package com.sweetcorner.incubyte_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for authentication response.
 * Contains the JWT token and user details sent back to the client after
 * successful login/registration.
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    /**
     * The JWT token string.
     */
    private String token;

    /**
     * The email of the authenticated user.
     */
    private String email;

    /**
     * The name of the authenticated user.
     */
    private String name;

    /**
     * The role of the authenticated user (e.g., "CUSTOMER", "ADMIN").
     */
    private String role;
}
