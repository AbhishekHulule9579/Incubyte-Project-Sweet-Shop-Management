package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing an Administrator in the system.
 * Admins have special privileges to manage sweets and view orders.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin {
    /**
     * Unique identifier for the Admin.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email address of the admin. Must be unique.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Encrypted password of the admin.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Full name of the admin.
     */
    private String fullName;
}
