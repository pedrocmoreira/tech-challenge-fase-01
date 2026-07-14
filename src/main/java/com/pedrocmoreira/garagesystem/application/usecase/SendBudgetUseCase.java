package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import com.pedrocmoreira.garagesystem.infrastructure.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendBudgetUseCase {
    private final ServiceOrderRepository serviceOrderRepository;
    private final EmailService emailService;

    @Transactional
    public ServiceOrder execute(Long serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.filterById(serviceOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", serviceOrderId));

        serviceOrder.nextStatus(StatusSO.AGUARDANDO_APROVACAO);
        serviceOrder.setBudgetToken(UUID.randomUUID().toString());
        ServiceOrder savedServiceOrder = serviceOrderRepository.save(serviceOrder);

        emailService.sendBudget(savedServiceOrder);

        return savedServiceOrder;
    }
}
