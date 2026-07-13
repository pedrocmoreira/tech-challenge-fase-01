package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceOrder {
    private Long id;
    private String number; // ex: OS-2024-00001
    private Customer customer;
    private Vehicle vehicle;

    @Builder.Default
    private StatusSO status = StatusSO.RECEBIDA;

    @Builder.Default
    private BigDecimal totalValue = BigDecimal.ZERO;

    private String observations;

    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();

    private LocalDateTime diagnosisStartDate;
    private LocalDateTime executionStartDate;
    private LocalDateTime completionDate;
    private LocalDateTime deliveryDate;

    @Builder.Default
    private List<ServiceItem> serviceItems = new ArrayList<>();

    @Builder.Default
    private List<PartItem> partItems = new ArrayList<>();

    public void timestampRegister(StatusSO newStatus) {
        LocalDateTime now = LocalDateTime.now();
        switch (newStatus) {
            case EM_DIAGNOSTICO -> this.diagnosisStartDate = now;
            case EM_EXECUCAO -> this.executionStartDate = now;
            case FINALIZADA -> this.completionDate = now;
            case ENTREGUE -> this.deliveryDate = now;
            default -> {}
        }
    }

    public void nextStatus(StatusSO newStatus) {
        this.status = this.getStatus().transitionTo(newStatus);
        timestampRegister(newStatus);
    }

    public void recalculateTotal() {
        BigDecimal servicesTotal = serviceItems.stream()
                .map(ServiceItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal partsTotal = partItems.stream()
                .map(PartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalValue = servicesTotal.add(partsTotal);
    }

    public void addItemService(ServiceItem item) {
        this.serviceItems.add(item);
        recalculateTotal();
    }

    public void addPartItem(PartItem item) {
        this.partItems.add(item);
        recalculateTotal();
    }

    public Long getExecutionTimeInMinutes() {
        if (executionStartDate == null || completionDate == null) return null;
        return java.time.Duration.between(executionStartDate, completionDate).toMinutes();
    }
}