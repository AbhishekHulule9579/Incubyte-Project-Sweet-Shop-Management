package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a user of the system (Customer).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    /**
     * Unique identifier for the User.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The email address of the user. Must be unique.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The encrypted password of the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The role of the user (e.g., "CUSTOMER").
     */
    @Column(nullable = false)
    private String role; // "ADMIN" or "CUSTOMER"
}
