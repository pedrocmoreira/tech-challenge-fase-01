package com.pedrocmoreira.garagesystem.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleDTO {
    public record Request(
            @NotBlank(message = "Placa é obrigatória")
            String plate,

            @NotBlank(message = "Marca é obrigatória")
            String make,

            @NotBlank(message = "Modelo é obrigatório")
            String model,

            @NotNull(message = "Ano é obrigatório")
            Integer year,

            String color,

            @NotNull(message = "Cliente é obrigatório")
            Long customerId
    ){ }

    public record Response(
        Long id,
        String plate,
        String make,
        String model,
        Integer ano,
        String color,
        Long customerId,
        String customerName
    ){}
}
