package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Category entity.
 * Provides data access operations for product categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds a category by its name.
     *
     * @param name The name of the category.
     * @return An Optional containing the Category if found, or empty otherwise.
     */
    Optional<Category> findByName(String name);
}
