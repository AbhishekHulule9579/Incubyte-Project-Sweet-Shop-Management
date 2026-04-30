package com.sweetcorner.incubyte_backend.repository;

import com.sweetcorner.incubyte_backend.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SweetRepository extends JpaRepository<Sweet, Long> {
        List<Sweet> findByCategoryName(String categoryName);
        List<Sweet> findByNameContainingIgnoreCase(String name);
}
