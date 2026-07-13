package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PartItem {
    private Long id;
    private ServiceOrder serviceOrder;
    private Part part;
    private Integer quantity;
    private BigDecimal unitPriceApplied;

    public BigDecimal getSubtotal(){
        return unitPriceApplied.multiply(BigDecimal.valueOf(quantity));

    }
}
