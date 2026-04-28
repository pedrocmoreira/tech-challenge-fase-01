package com.pedrocmoreira.garagesystem.domain.model;

import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(length = 50)
    private String code; //Código interno ou o código do fabricante

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
