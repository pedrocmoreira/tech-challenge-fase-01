package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.exception.InvalidStatusTransitionException;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetApproveUseCase {
    private final ServiceOrderRepository serviceOrderRepository;

    private ServiceOrder searchById(Long serviceOrderId) {
        return serviceOrderRepository.filterById(serviceOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", serviceOrderId));
    }

    @Transactional
    public ServiceOrder approve(Long serviceOrderId) {
        ServiceOrder serviceOrder = searchById(serviceOrderId);
        if (serviceOrder.getStatus() != StatusSO.AGUARDANDO_APROVACAO) {
            throw new InvalidStatusTransitionException(
                    "O orçamento só pode ser aprovado quanto a ordem de serviço está em AGUARDANDO_APROVACAO. " +
                            "Status atual: " + serviceOrder.getStatus()
            );
        }

        serviceOrder.nextStatus(StatusSO.EM_EXECUCAO);
        serviceOrder.setBudgetToken(null);
        return serviceOrderRepository.save(serviceOrder);
    }

    @Transactional
    public ServiceOrder refuse(Long serviceOrderId) {
        ServiceOrder serviceOrder = searchById(serviceOrderId);
        if(serviceOrder.getStatus() != StatusSO.AGUARDANDO_APROVACAO) {
            throw new InvalidStatusTransitionException(
                    "O orçamento só pode ser aprovado quanto a ordem de serviço está em AGUARDANDO_APROVACAO. " +
                            "Status atual: " + serviceOrder.getStatus()
            );
        }

        serviceOrder.nextStatus(StatusSO.CANCELADA);
        serviceOrder.setBudgetToken(null);
        return serviceOrderRepository.save(serviceOrder);
    }
}
