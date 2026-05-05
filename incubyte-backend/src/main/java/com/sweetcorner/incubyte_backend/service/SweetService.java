package com.sweetcorner.incubyte_backend.service;

import com.sweetcorner.incubyte_backend.dto.SweetRequest;
import com.sweetcorner.incubyte_backend.entity.Category;
import com.sweetcorner.incubyte_backend.entity.Sweet;
import com.sweetcorner.incubyte_backend.entity.SweetOrder;
import com.sweetcorner.incubyte_backend.entity.SweetOrderDetail;
import com.sweetcorner.incubyte_backend.repository.CategoryRepository;
import com.sweetcorner.incubyte_backend.repository.SweetOrderRepository;
import com.sweetcorner.incubyte_backend.repository.SweetRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;
    private final CategoryRepository categoryRepository;
    private final SweetOrderRepository sweetOrderRepository;

    public SweetService(SweetRepository sweetRepository, CategoryRepository categoryRepository,
            SweetOrderRepository sweetOrderRepository) {
        this.sweetRepository = sweetRepository;
        this.categoryRepository = categoryRepository;
        this.sweetOrderRepository = sweetOrderRepository;
    }

    // Cache the entire catalog
    @Cacheable(value = "products", key="'all'")
    public List<Sweet> getAllSweets() {
        System.out.println("Fetching sweets from postgres"); 
        return sweetRepository.findAll();
    }

    // Cache categories
    @Cacheable(value = "category", key="#categoryName")
    public List<Sweet> getSweetsByCategory(String categoryName) {
        System.out.println("Fetching category " + categoryName + " from postgres"); 
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

    // --- REDIS CACHING FOR LOGGED IN USERS ---

    // 1. Evict (Delete) Cache when a purchase is made
    @CacheEvict(value = "userOrders", key="#userEmail")
    @Transactional
    public void purchaseSweets(List<Map<String, Object>> items, String userEmail) {
        SweetOrder order = new SweetOrder();
        order.setUserEmail(userEmail);
        order.setOrderDate(LocalDateTime.now());

        List<SweetOrderDetail> details = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map<String, Object> item : items) {
            Long id = ((Number) item.get("id")).longValue();
            Integer quantityToBuy = ((Number) item.get("quantity")).intValue();

            Sweet sweet = sweetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sweet not found: " + id));

            if (sweet.getQuantity() < quantityToBuy) {
                throw new RuntimeException("Insufficient stock for sweet: " + sweet.getName());
            }

            sweet.setQuantity(sweet.getQuantity() - quantityToBuy);
            sweetRepository.save(sweet);

            SweetOrderDetail detail = new SweetOrderDetail();
            detail.setSweetOrder(order);
            detail.setSweetName(sweet.getName());
            detail.setImageUrl(sweet.getImageUrl());
            detail.setQuantity(quantityToBuy);
            detail.setPrice(sweet.getPrice());

            details.add(detail);
            totalAmount = totalAmount.add(sweet.getPrice().multiply(new BigDecimal(quantityToBuy)));
        }

        order.setTotalAmount(totalAmount);
        order.setOrderDetails(details);
        sweetOrderRepository.save(order);
    }

    // 2. Cache the Orders for fast loading on the frontend
    @Cacheable(value = "userOrders", key = "#userEmail")
    public List<SweetOrder> getUserOrders(String userEmail) {
        System.out.println("Fetching order history for " + userEmail + " from PostgreSQL!"); 
        return sweetOrderRepository.findByUserEmailOrderByOrderDateDesc(userEmail);
    }
}
