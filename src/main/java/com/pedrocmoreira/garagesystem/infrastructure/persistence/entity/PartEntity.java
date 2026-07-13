package com.pedrocmoreira.garagesystem.infrastructure.persistence.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(length = 50)
    private String code;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @PositiveOrZero
    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column
    @Builder.Default
    private Integer minStock = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
