package com.sweetcorner.incubyte_backend.config;

import com.sweetcorner.incubyte_backend.entity.Category;
import com.sweetcorner.incubyte_backend.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component responsible for initializing default data in the database on
 * application startup.
 * Specifically, it creates default product categories if they do not exist.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param categoryRepository Repository for accessing Category data.
     */
    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Method executed after the application context is loaded.
     * It ensures default categories are present in the database.
     *
     * @param args Command line arguments.
     * @throws Exception If an error occurs during execution.
     */
    @Override
    public void run(String... args) throws Exception {
        createCategoryIfNotFound("Sweets");
        createCategoryIfNotFound("Namkeen");
        createCategoryIfNotFound("Gifting");
        createCategoryIfNotFound("Dry-Fruits");
    }

    /**
     * Helper method to check if a category exists and create it if not.
     *
     * @param name The name of the category to check/create.
     */
    private void createCategoryIfNotFound(String name) {
        if (categoryRepository.findByName(name).isEmpty()) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
    }
}
