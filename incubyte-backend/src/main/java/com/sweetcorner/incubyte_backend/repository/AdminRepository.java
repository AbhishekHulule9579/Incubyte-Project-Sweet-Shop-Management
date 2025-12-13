package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);

    Boolean existsByEmail(String email);
}
