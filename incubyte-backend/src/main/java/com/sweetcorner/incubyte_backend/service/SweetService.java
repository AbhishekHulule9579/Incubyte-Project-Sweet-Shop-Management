package com.sweetcorner.incubyte_backend.service;

import com.sweetcorner.incubyte_backend.dto.SweetRequest;
import com.sweetcorner.incubyte_backend.entity.Category;
import com.sweetcorner.incubyte_backend.entity.Sweet;
import com.sweetcorner.incubyte_backend.repository.CategoryRepository;
import com.sweetcorner.incubyte_backend.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;
    private final CategoryRepository categoryRepository;

    public SweetService(SweetRepository sweetRepository, CategoryRepository categoryRepository) {
        this.sweetRepository = sweetRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    public Sweet addSweet(SweetRequest request) {
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryName()));

        Sweet sweet = new Sweet();
        sweet.setName(request.getName());
        sweet.setDescription(request.getDescription());
        sweet.setPrice(request.getPrice());
        sweet.setQuantity(request.getQuantity());
        sweet.setImageUrl(request.getImageUrl());
        sweet.setCategory(category);

        return sweetRepository.save(sweet);
    }

    public Sweet restockSweet(Long id, Integer quantityToAdd) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));

        sweet.setQuantity(sweet.getQuantity() + quantityToAdd);
        return sweetRepository.save(sweet);
    }

    public void deleteSweet(Long id) {
        if (!sweetRepository.existsById(id)) {
            throw new RuntimeException("Sweet not found");
        }
        sweetRepository.deleteById(id);
    }
}
