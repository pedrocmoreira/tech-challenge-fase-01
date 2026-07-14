package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceItem {
    private Long id;
    private ServiceOrder serviceOrder;
    private Service service;
    private BigDecimal appliedPrice; //Preço no momento da SO

    public BigDecimal getSubtotal() {
        return appliedPrice;
    }
}
