package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Sweet entity.
 * Handles database interactions for sweet products.
 */
@Repository
public interface SweetRepository extends JpaRepository<Sweet, Long> {
        /**
         * Finds sweets belonging to a specific category.
         *
         * @param categoryName The name of the category.
         * @return A list of Sweet objects in that category.
         */
        List<Sweet> findByCategoryName(String categoryName);

        /**
         * Searches for sweets whose name contains the specified string, ignoring case.
         *
         * @param name The partial name to search for.
         * @return A list of matching Sweet objects.
         */
        List<Sweet> findByNameContainingIgnoreCase(String name);
}
