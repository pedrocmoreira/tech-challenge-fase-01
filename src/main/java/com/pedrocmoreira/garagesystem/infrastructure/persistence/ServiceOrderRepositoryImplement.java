package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.ServiceOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServiceOrderRepositoryImplement implements ServiceOrderRepository {
    private final ServiceOrderJpaRepository jpa;

    @Override
    public ServiceOrder save(ServiceOrder serviceOrder) {
        return ServiceOrderMapper.toDomain(jpa.save(ServiceOrderMapper.toEntity(serviceOrder)));
    }

    @Override
    public Optional<ServiceOrder> filterById(Long id) {
        return jpa.findById(id).map(ServiceOrderMapper::toDomain);
    }

    @Override
    public Optional<ServiceOrder> filterByNumber(String number) {
        return jpa.findByNumber(number).map(ServiceOrderMapper::toDomain);
    }

    @Override
    public Optional<ServiceOrder> filterByBudgetToken(String budgetToken) {
        return jpa.findByBudgetToken(budgetToken).map(ServiceOrderMapper::toDomain);
    }

    @Override
    public List<ServiceOrder> listAll() {
        return jpa.findAll().stream().map(ServiceOrderMapper::toDomain).toList();
    }

    @Override
    public List<ServiceOrder> listByStatus(StatusSO statusSO) {
        return jpa.findByStatus(statusSO).stream().map(ServiceOrderMapper::toDomain).toList();
    }

    @Override
    public List<ServiceOrder> listByCustomer(Long customerId) {
        return jpa.findByCustomerId(customerId).stream().map(ServiceOrderMapper::toDomain).toList();
    }

    @Override
    public String generateNextNumber() {
        int sequence = jpa.findMaxSequencial() + 1;
        return String.format("SO-%d-%05d", Year.now().getValue(), sequence);
    }
}