package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrder, Long> {
    Optional<ServiceOrder> findByNumber(String number);
    List<ServiceOrder> findByStatus(StatusSO statusSO);
    List<ServiceOrder> findByCustomerId(Long customerId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.number, 9) AS int)), 0) FROM ServiceOrder o")
    int findMaxSequencial();
}
