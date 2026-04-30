package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.SweetOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SweetOrderRepository extends JpaRepository<SweetOrder, Long> {
    List<SweetOrder> findByUserEmailOrderByOrderDateDesc(String userEmail);
}
