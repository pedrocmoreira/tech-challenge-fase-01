package com.pedrocmoreira.garagesystem.domain.repository;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderRepository {
    ServiceOrder save(ServiceOrder serviceOrder);
    Optional<ServiceOrder> filterById(Long id);
    Optional<ServiceOrder> filterByNumber(String number);
    Optional<ServiceOrder> filterByBudgetToken(String budgetToken);
    List<ServiceOrder> listAll();
    List<ServiceOrder> listByStatus(StatusSO statusSO);
    List<ServiceOrder> listByCustomer(Long customerId);
    String generateNextNumber();
}
