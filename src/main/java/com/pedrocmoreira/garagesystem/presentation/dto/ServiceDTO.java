package com.pedrocmoreira.garagesystem.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ServiceDTO {
    public record  Request(
            @NotBlank(message = "Nome é obrigatório")
            String name,

            String description,

            @NotNull(message = "Preço base é obrigatório")
            BigDecimal basePrice,

            Integer estimedTimeInMinutes
    ){}

    public record Response (
            Long id,
            String name,
            String description,
            BigDecimal basePrice,
            Integer estimedTimeInminutes,
            Boolean active
    ){}
}
