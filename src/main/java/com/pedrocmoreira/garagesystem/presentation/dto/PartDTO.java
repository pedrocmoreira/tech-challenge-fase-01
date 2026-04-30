package com.pedrocmoreira.garagesystem.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PartDTO {
    public record Request(
            @NotBlank(message = "Nome é obrigatório")
            String name,

            String description,
            String code,

            @NotNull(message = "Preço unitário é obrigatório")
            BigDecimal unitPrice,

            @PositiveOrZero
            Integer quantityStock,

            Integer minStock
    ){}

    public record Response (
            Long id,
            String name,
            String description,
            String code,
            BigDecimal unitPrice,
            Integer quantityStock,
            Integer minStock,
            Boolean active,
            Boolean criticStock
    ){}
}
