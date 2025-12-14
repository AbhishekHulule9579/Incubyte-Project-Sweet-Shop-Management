package com.sweetcorner.incubyte_backend.service;

import com.sweetcorner.incubyte_backend.dto.SweetRequest;
import com.sweetcorner.incubyte_backend.entity.Category;
import com.sweetcorner.incubyte_backend.entity.Sweet;
import com.sweetcorner.incubyte_backend.repository.CategoryRepository;
import com.sweetcorner.incubyte_backend.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing sweets and orders.
 * Handles the business logic for creating, updating, retrieving, restocking,
 * and purchasing sweets.
 */
@Service
public class SweetService {

    private final SweetRepository sweetRepository;
    private final CategoryRepository categoryRepository;
    private final com.sweetcorner.incubyte_backend.repository.SweetOrderRepository sweetOrderRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param sweetRepository      Repository for accessing Sweet data.
     * @param categoryRepository   Repository for accessing Category data.
     * @param sweetOrderRepository Repository for accessing SweetOrder data.
     */
    public SweetService(SweetRepository sweetRepository, CategoryRepository categoryRepository,
            com.sweetcorner.incubyte_backend.repository.SweetOrderRepository sweetOrderRepository) {
        this.sweetRepository = sweetRepository;
        this.categoryRepository = categoryRepository;
        this.sweetOrderRepository = sweetOrderRepository;
    }

    /**
     * Retrieves all sweets from the database.
     *
     * @return A list of all available sweets.
     */
    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    /**
     * Retrieves sweets by their category name.
     *
     * @param categoryName The name of the category to filter by.
     * @return A list of sweets in the specified category.
     */
    public List<Sweet> getSweetsByCategory(String categoryName) {
        return sweetRepository.findByCategoryName(categoryName);
    }

    /**
     * Adds a new sweet to the inventory.
     *
     * @param request The request containing details of the sweet to add.
     * @return The newly created Sweet object.
     * @throws RuntimeException if the specified category is not found.
     */
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

    /**
     * Updates an existing sweet's details.
     *
     * @param id      The ID of the sweet to update.
     * @param request The new details for the sweet.
     * @return The updated Sweet object.
     * @throws RuntimeException if the sweet or category is not found.
     */
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

    /**
     * Increases the quantity of a sweet in stock.
     *
     * @param id            The ID of the sweet to restock.
     * @param quantityToAdd The amount to add to the current stock.
     * @return The updated Sweet object.
     * @throws RuntimeException if the sweet is not found.
     */
    public Sweet restockSweet(Long id, Integer quantityToAdd) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));

        sweet.setQuantity(sweet.getQuantity() + quantityToAdd);
        return sweetRepository.save(sweet);
    }

    /**
     * Deletes a sweet from the database.
     *
     * @param id The ID of the sweet to delete.
     * @throws RuntimeException if the sweet is not found.
     */
    public void deleteSweet(Long id) {
        if (!sweetRepository.existsById(id)) {
            throw new RuntimeException("Sweet not found");
        }
        sweetRepository.deleteById(id);
    }

    /**
     * Processes a purchase transaction.
     * It updates the stock quantity for each purchased item and saves an order
     * record.
     *
     * @param items     List of maps containing item ID and quantity.
     * @param userEmail The email of the user making the purchase.
     * @throws RuntimeException if a sweet is not found or if there is insufficient
     *                          stock.
     */
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

            // Deduct stock
            sweet.setQuantity(sweet.getQuantity() - quantityToBuy);
            sweetRepository.save(sweet);

            // Create Order Detail
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

    /**
     * Retrieves the order history for a specific user.
     *
     * @param userEmail The email of the user.
     * @return A list of past orders for the user.
     */
    public java.util.List<com.sweetcorner.incubyte_backend.entity.SweetOrder> getUserOrders(String userEmail) {
        return sweetOrderRepository.findByUserEmailOrderByOrderDateDesc(userEmail);
    }
}
