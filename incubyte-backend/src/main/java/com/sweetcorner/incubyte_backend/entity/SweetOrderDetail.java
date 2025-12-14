package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity representing the details of a specific item within an order.
 * Captures the state of the product at the time of purchase (name, price,
 * etc.).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sweet_order_details")
public class SweetOrderDetail {
    /**
     * Unique identifier for the Order Detail.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The order to which this detail belongs.
     */
    @ManyToOne
    @JoinColumn(name = "sweet_order_id")
    @JsonIgnore
    private SweetOrder sweetOrder;

    /**
     * The name of the sweet purchased.
     */
    private String sweetName;

    /**
     * The image URL of the sweet purchased.
     */
    private String imageUrl;

    /**
     * The quantity purchased.
     */
    private Integer quantity;

    /**
     * The price per unit at the time of purchase.
     */
    private BigDecimal price;
}
