package com.pedrocmoreira.garagesystem.infrastructure.persistence.entity;

import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(name = "budget_token", unique = true, length = 36)
    private String budgetToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

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
    private List<ServiceItemEntity> serviceItems = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PartItemEntity> partItems = new ArrayList<>();
}