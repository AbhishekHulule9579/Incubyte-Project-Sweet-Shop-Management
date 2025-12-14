package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a customer's order.
 * Stores order details such as the user, date, total amount, and list of items.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sweet_orders")
public class SweetOrder {
    /**
     * Unique identifier for the Order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The email of the user who placed the order.
     */
    private String userEmail;

    /**
     * The date and time when the order was placed.
     */
    private LocalDateTime orderDate;

    /**
     * The total monetary amount of the order.
     */
    private BigDecimal totalAmount;

    /**
     * List of details (items) included in this order.
     */
    @OneToMany(mappedBy = "sweetOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SweetOrderDetail> orderDetails = new ArrayList<>();
}
