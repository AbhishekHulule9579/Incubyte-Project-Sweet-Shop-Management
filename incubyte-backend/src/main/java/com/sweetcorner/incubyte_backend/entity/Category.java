package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a product category (e.g., Sweets, Namkeen).
 * Categories are used to organize sweets.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {
    /**
     * Unique identifier for the Category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the category. Must be unique.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * A description of the category.
     */
    private String description;

    /**
     * URL for the category's image.
     */
    private String imageUrl;
}
