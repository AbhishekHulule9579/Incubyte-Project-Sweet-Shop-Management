package com.sweetcorner.incubyte_backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SweetRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private String categoryName;
}
