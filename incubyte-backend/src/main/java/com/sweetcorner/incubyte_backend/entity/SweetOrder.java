package com.sweetcorner.incubyte_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sweet_orders")
public class SweetOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    @OneToMany(mappedBy = "sweetOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SweetOrderDetail> orderDetails = new ArrayList<>();
}
