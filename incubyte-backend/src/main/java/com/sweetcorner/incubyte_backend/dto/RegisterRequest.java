package com.sweetcorner.incubyte_backend.dto;

import lombok.Data;

/**
 * Data Transfer Object for user registration requests.
 * Captures the necessary information to register a new user.
 */
@Data
public class RegisterRequest {
    /**
     * The name of the user being registered.
     */
    private String name;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The password for the new account.
     */
    private String password;
}
