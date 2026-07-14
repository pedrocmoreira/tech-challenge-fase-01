package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    private Long id;
    private String document; // CPF ou CNPJ (digitos) ###TODO adicionar validador de CPF CNPJ
    private DocumentType documentType;
    private String name;
    private String phone;
    private String email;
    private List<Vehicle> vehicles = new ArrayList<>();

    public enum DocumentType {CPF, CNPJ}
}
