package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ListActiveServiceOrdersUseCase {
    private final ServiceOrderRepository serviceOrderRepository;

    private static final Set<StatusSO> EXCLUDED_FROM_LISTING = Set.of(StatusSO.FINALIZADA, StatusSO.ENTREGUE);

    @Transactional(readOnly = true)
    public List<ServiceOrder> execute() {
        return serviceOrderRepository.listAll().stream()
                .filter(serviceOrder -> !EXCLUDED_FROM_LISTING.contains(serviceOrder.getStatus()))
                .sorted(
                        Comparator.comparingInt((ServiceOrder serviceOrder) -> serviceOrder.getStatus().listingPriority())
                                .thenComparing(ServiceOrder::getCreated_at)
                )
                .toList();
    }
}
