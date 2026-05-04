package com.pedrocmoreira.garagesystem.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number; //ex: OS-2024-00001

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StatusSO status = StatusSO.RECEBIDA;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();

    @Column
    private LocalDateTime diagnosisStartDate;

    @Column
    private LocalDateTime executionStartDate;

    @Column
    private LocalDateTime completionDate;

    @Column
    private LocalDateTime deliveryDate;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ServiceItem> serviceItems = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PartItem> partItems = new ArrayList<>();

    public void timestampRegister(StatusSO newStatus){
        LocalDateTime now = LocalDateTime.now();
        switch (newStatus){
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

    public void recalculateTotal(){
        BigDecimal servicesTotal = serviceItems.stream()
                .map(ServiceItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal partsTotal = partItems.stream()
                .map(PartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalValue = servicesTotal.add(partsTotal);
    }

    public void addItemService(ServiceItem item){
        item.setServiceOrder(this);
        this.serviceItems.add(item);
        recalculateTotal();
    }

    public void addPartItem(PartItem item){
        item.setServiceOrder(this);
        this.partItems.add(item);
        recalculateTotal();
    }

    public Long getExecutionTimeInMinutes(){
        if(executionStartDate == null || completionDate == null) return null;
        return java.time.Duration.between(executionStartDate, completionDate).toMinutes();
    }
}
