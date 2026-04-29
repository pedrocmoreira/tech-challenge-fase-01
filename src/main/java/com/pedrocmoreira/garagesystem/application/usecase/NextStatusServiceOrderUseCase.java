package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NextStatusServiceOrderUseCase {
    private final ServiceOrderRepository serviceOrderRepository;

    @Transactional
    public ServiceOrder execute(Long serviceOrderId, StatusSO newStatus) {
        ServiceOrder serviceOrder = serviceOrderRepository.filterById(serviceOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", serviceOrderId));
        serviceOrder.nextStatus(newStatus);
        return serviceOrderRepository.save(serviceOrder);
    }
}
