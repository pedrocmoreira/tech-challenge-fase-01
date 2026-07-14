package com.pedrocmoreira.garagesystem.domain.model;

import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Part {
    private Long id;
    private String name;
    private String description;
    private String code; //Código interno ou o código do fabricante
    private BigDecimal unitPrice;
    @Builder.Default
    private Integer stockQuantity = 0;
    @Builder.Default
    private Integer minStock = 0;
    @Builder.Default
    private Boolean active = true;

    public void decrement(int quantity) {
        if(this.stockQuantity < quantity) {
            throw new InsufficientStockException(this.name, this.stockQuantity, quantity);
        }

        this.stockQuantity -= quantity;
    }

    public void replace(int quantity) {
       this.stockQuantity += quantity;
    }

    public boolean hasStock(int quantity){
        return this.stockQuantity >= quantity;
    }

    public boolean criticStock() {
        return this.stockQuantity <= this.minStock;
    }
}
