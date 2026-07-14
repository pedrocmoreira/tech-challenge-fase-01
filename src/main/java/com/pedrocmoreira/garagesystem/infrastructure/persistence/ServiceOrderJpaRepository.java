package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, Long> {
    Optional<ServiceOrderEntity> findByNumber(String number);
    Optional<ServiceOrderEntity> findByBudgetToken(String budgetToken);
    List<ServiceOrderEntity> findByStatus(StatusSO statusSO);
    List<ServiceOrderEntity> findByCustomerId(Long customerId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.number, 9) AS int)), 0) FROM ServiceOrderEntity o")
    int findMaxSequencial();
}