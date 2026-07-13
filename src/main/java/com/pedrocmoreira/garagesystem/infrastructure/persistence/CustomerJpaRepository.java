package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByDocument(String document);
    boolean existsByDocument(String document);
}