package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartJpaRepository  extends JpaRepository<Part, Long> {
    List<Part> findByActiveTrue();

    @Query("SELECT p FROM Part p WHERE p.stockQuantity <= p.minStock AND p.active = true")
    List<Part> findCriticStock();
}
