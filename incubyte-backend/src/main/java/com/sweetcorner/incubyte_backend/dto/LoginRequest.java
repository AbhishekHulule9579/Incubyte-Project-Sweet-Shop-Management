package com.sweetcorner.incubyte_backend.dto;

import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 * Captures the credentials provided by the user during the login process.
 */
@Data
public class LoginRequest {
    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role of the user (e.g., "ADMIN" or "CUSTOMER").
     * Defaults to "CUSTOMER" if not specified.
     */
    private String role; // "ADMIN" or "CUSTOMER" (default to customer if null)
}
