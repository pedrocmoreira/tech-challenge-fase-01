package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartJpaRepository extends JpaRepository<PartEntity, Long> {
    List<PartEntity> findByActiveTrue();

    @Query("SELECT p FROM PartEntity p WHERE p.stockQuantity <= p.minStock AND p.active = true")
    List<PartEntity> findCriticStock();
}