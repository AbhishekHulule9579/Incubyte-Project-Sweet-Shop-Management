package com.sweetcorner.incubyte_backend.config;

import com.sweetcorner.incubyte_backend.entity.Category;
import com.sweetcorner.incubyte_backend.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createCategoryIfNotFound("Sweets");
        createCategoryIfNotFound("Namkeen");
        createCategoryIfNotFound("Gifting");
        createCategoryIfNotFound("Dry-Fruits");
    }

    private void createCategoryIfNotFound(String name) {
        if (categoryRepository.findByName(name).isEmpty()) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
    }
}
