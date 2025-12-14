package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.SweetOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for SweetOrder entity.
 * Manages database operations related to customer orders.
 */
public interface SweetOrderRepository extends JpaRepository<SweetOrder, Long> {
    /**
     * Retrieves a list of orders for a specific user, ordered by date (newest
     * first).
     *
     * @param userEmail The email of the user whose orders to retrieve.
     * @return A list of SweetOrder objects.
     */
    List<SweetOrder> findByUserEmailOrderByOrderDateDesc(String userEmail);
}
