package com.pedrocmoreira.garagesystem.domain.service;

import com.pedrocmoreira.garagesystem.domain.model.Customer.DocumentType;

public class DocumentValidator {
    private DocumentValidator(){}

    public static DocumentType detectType(String document) {
        if (document == null) throw new IllegalArgumentException("Documento não pode ser nulo.");
        String digits = document.replaceAll("\\D", "");
        return switch (digits.length()) {
            case 11 -> DocumentType.CPF;
            case 14 -> DocumentType.CNPJ;
            default -> throw new IllegalArgumentException(
                    "Documento inválido: deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ).");
        };
    }

    public static boolean isValid(String document) {
        if (document == null) return false;
        String digits = document.replaceAll("\\D", "");
        return switch (digits.length()) {
            case 11 -> CPFValidator.isValid(digits);
            case 14 -> CNPJValidator.isValid(digits);
            default -> false;
        };
    }
}
