package com.sweetcorner.incubyte_backend.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object for creating or updating a sweet product.
 * Contains all details required to define a sweet item.
 */
@Data
public class SweetRequest {
    /**
     * The name of the sweet.
     */
    private String name;

    /**
     * A description of the sweet.
     */
    private String description;

    /**
     * The price of the sweet per unit.
     */
    private BigDecimal price;

    /**
     * The available quantity of the sweet in stock.
     */
    private Integer quantity;

    /**
     * The URL of the sweet's image.
     */
    private String imageUrl;

    /**
     * The name of the category the sweet belongs to.
     */
    private String categoryName;
}
