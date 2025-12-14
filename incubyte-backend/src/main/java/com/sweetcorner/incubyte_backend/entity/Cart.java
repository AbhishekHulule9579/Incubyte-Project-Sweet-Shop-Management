package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a shopping cart.
 * A cart belongs to a user and contains multiple cart items.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    /**
     * Unique identifier for the Cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user to whom this cart belongs.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The total price of all items in the cart.
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * List of items contained in the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
}
