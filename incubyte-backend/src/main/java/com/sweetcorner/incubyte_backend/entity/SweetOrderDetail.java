package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sweet_order_details")
public class SweetOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sweet_order_id")
    @JsonIgnore
    private SweetOrder sweetOrder;

    private String sweetName;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;
}
