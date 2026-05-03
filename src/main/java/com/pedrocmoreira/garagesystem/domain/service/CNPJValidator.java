package com.pedrocmoreira.garagesystem.domain.service;

public class CNPJValidator {

    private CNPJValidator() {}

    private static final int[] PESO1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESO2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static boolean verifyDigit(String digits, int[] pesos, int pos) {
        int sum = 0;
        for (int i = 0; i < pesos.length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * pesos[i];
        }
        int remainder = sum % 11;
        int expected = remainder < 2 ? 0 : 11 - remainder;
        return expected == Character.getNumericValue(digits.charAt(pos));
    }

    public static boolean isValid(String cnpj) {
        if (cnpj == null) return false;

        String digits = cnpj.replaceAll("\\D", "");

        if (digits.length() != 14) return false;
        if (digits.chars().distinct().count() == 1) return false;

        return verifyDigit(digits, PESO1, 12) && verifyDigit(digits, PESO2, 13);
    }
}
