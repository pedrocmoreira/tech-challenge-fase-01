package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
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
        return jpa.save(serviceOrder);
    }

    @Override
    public Optional<ServiceOrder> filterById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<ServiceOrder> filterByNumber(String number) {
        return jpa.findByNumber(number);
    }

    @Override
    public List<ServiceOrder> listAll() {
        return jpa.findAll();
    }

    @Override
    public List<ServiceOrder> listByStatus(StatusSO statusSO) {
        return jpa.findByStatus(statusSO);
    }

    @Override
    public List<ServiceOrder> listByCustomer(Long customerId) {
        return jpa.findByCustomerId(customerId);
    }

    @Override
    public String generateNextNumber() {
        int sequence = jpa.findMaxSequencial() + 1;
        return String.format("SO-%d-%05d", Year.now().getValue(), sequence);
    }
}
