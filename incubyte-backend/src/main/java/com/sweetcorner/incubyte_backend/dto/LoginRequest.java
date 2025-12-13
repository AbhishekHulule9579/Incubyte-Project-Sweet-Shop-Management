package com.sweetcorner.incubyte_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String role; // "ADMIN" or "CUSTOMER" (default to customer if null)
}
