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
    private final com.sweetcorner.incubyte_backend.repository.SweetOrderRepository sweetOrderRepository;
    public SweetService(SweetRepository sweetRepository, CategoryRepository categoryRepository,
            com.sweetcorner.incubyte_backend.repository.SweetOrderRepository sweetOrderRepository) {
        this.sweetRepository = sweetRepository;
        this.categoryRepository = categoryRepository;
        this.sweetOrderRepository = sweetOrderRepository;
    }
    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }
    public List<Sweet> getSweetsByCategory(String categoryName) {
        return sweetRepository.findByCategoryName(categoryName);
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
    public Sweet updateSweet(Long id, SweetRequest request) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));

        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryName()));

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
    @org.springframework.transaction.annotation.Transactional
    public void purchaseSweets(List<java.util.Map<String, Object>> items, String userEmail) {
        com.sweetcorner.incubyte_backend.entity.SweetOrder order = new com.sweetcorner.incubyte_backend.entity.SweetOrder();
        order.setUserEmail(userEmail);
        order.setOrderDate(java.time.LocalDateTime.now());

        java.util.List<com.sweetcorner.incubyte_backend.entity.SweetOrderDetail> details = new java.util.ArrayList<>();
        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

        for (java.util.Map<String, Object> item : items) {
            Long id = ((Number) item.get("id")).longValue();
            Integer quantityToBuy = ((Number) item.get("quantity")).intValue();

            Sweet sweet = sweetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sweet not found: " + id));

            if (sweet.getQuantity() < quantityToBuy) {
                throw new RuntimeException("Insufficient stock for sweet: " + sweet.getName());
            }

            sweet.setQuantity(sweet.getQuantity() - quantityToBuy);
            sweetRepository.save(sweet);

            com.sweetcorner.incubyte_backend.entity.SweetOrderDetail detail = new com.sweetcorner.incubyte_backend.entity.SweetOrderDetail();
            detail.setSweetOrder(order);
            detail.setSweetName(sweet.getName());
            detail.setImageUrl(sweet.getImageUrl());
            detail.setQuantity(quantityToBuy);
            detail.setPrice(sweet.getPrice());

            details.add(detail);
            totalAmount = totalAmount.add(sweet.getPrice().multiply(new java.math.BigDecimal(quantityToBuy)));
        }

        order.setTotalAmount(totalAmount);
        order.setOrderDetails(details);
        sweetOrderRepository.save(order);
    }
    public java.util.List<com.sweetcorner.incubyte_backend.entity.SweetOrder> getUserOrders(String userEmail) {
        return sweetOrderRepository.findByUserEmailOrderByOrderDateDesc(userEmail);
    }
}
