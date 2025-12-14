package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

/**
 * Repository interface for Admin entity.
 * Provides CRUD operations and custom queries for managing admins.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    /**
     * Finds an admin by their email address.
     *
     * @param email The email to search for.
     * @return An Optional containing the Admin if found, or empty otherwise.
     */
    Optional<Admin> findByEmail(String email);

    /**
     * Checks if an admin exists with the given email.
     *
     * @param email The email to check.
     * @return True if an admin exists with the email, false otherwise.
     */
    Boolean existsByEmail(String email);
}
