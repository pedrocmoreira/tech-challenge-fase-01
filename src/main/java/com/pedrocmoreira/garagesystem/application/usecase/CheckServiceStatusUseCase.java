package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckServiceStatusUseCase {
    private final ServiceOrderRepository serviceOrderRepository;

    @Transactional(readOnly = true)
    public ServiceOrder execute(String number) {
        return serviceOrderRepository.filterByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", number));
    }
}
