package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * Entity representing a sweet/product in the shop.
 * Contains details like name, price, quantity, and category.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sweets")
public class Sweet {
    /**
     * Unique identifier for the Sweet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the sweet.
     */
    @Column(nullable = false)
    private String name;

    /**
     * A detailed description of the sweet.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The price of the sweet per unit.
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * The quantity of the sweet available in stock.
     */
    @Column(nullable = false)
    private Integer quantity; // Available stock

    /**
     * URL of the image displaying the sweet.
     */
    @Column(length = 2048)
    private String imageUrl;

    /**
     * The category to which this sweet belongs.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
