package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Service {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer estimatedTimeMinutes;
    @Builder.Default
    private Boolean active = true;
}
