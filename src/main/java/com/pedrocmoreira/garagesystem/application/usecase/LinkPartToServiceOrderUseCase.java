package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Part;
import com.pedrocmoreira.garagesystem.domain.model.PartItem;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.repository.PartRepository;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkPartToServiceOrderUseCase {
    private final ServiceOrderRepository serviceOrderRepository;
    private final PartRepository partRepository;

    @Transactional
    public ServiceOrder execute(Long serviceOrderId, Long partId, int quantity){
        ServiceOrder serviceOrder = serviceOrderRepository.filterById(serviceOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", serviceOrderId));

        Part part = partRepository.filterById(partId)
                .orElseThrow(() -> new EntityNotFoundException("Peça", partId));

        part.decrement(quantity);
        partRepository.save(part);

        serviceOrder.addPartItem(PartItem.builder()
                .part(part)
                .quantity(quantity)
                .unitPriceApplied(part.getUnitPrice())
                .build()
        );

        return serviceOrderRepository.save(serviceOrder);
    }
}
