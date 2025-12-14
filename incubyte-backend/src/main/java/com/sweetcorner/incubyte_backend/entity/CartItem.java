package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * Entity representing an item within a shopping cart.
 * Links a specific sweet product to a cart with a quantity and price.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    /**
     * Unique identifier for the CartItem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The cart to which this item belongs.
     */
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /**
     * The sweet start product being purchased.
     */
    @ManyToOne
    @JoinColumn(name = "sweet_id")
    private Sweet sweet;

    /**
     * The quantity of the sweet.
     */
    private Integer quantity;

    /**
     * The price of the sweet at the time it was added to the cart.
     */
    private BigDecimal price; // Price at the time of adding
}
