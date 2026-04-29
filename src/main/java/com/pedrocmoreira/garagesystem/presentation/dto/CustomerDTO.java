package com.pedrocmoreira.garagesystem.presentation.dto;

import com.pedrocmoreira.garagesystem.domain.model.Customer.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CustomerDTO {
    public record Request(
            @NotBlank(message = "O documento é obrigatório")
            String document,

            @NotNull(message =  "Tipo de documento é obrigatório")
            DocumentType documentType,

            @NotBlank(message = "Nome é obrigatório")
            String name,

            String phone,
            String email
    ){}

    public record Response(
            Long id,
            String document,
            DocumentType documentType,
            String name,
            String phone,
            String email
    ){}
}
