package com.pedrocmoreira.garagesystem.domain.service;

public class CPFValidator {
    private CPFValidator() {}
    private  static boolean verifyDigit(String digits, int pos){
        int sum = 0;
        for(int i = 0; i < pos; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * (pos + 1 - i);
        }

        int remainder = (sum * 10) % 11;
        int expected = remainder == 10 ? 0 : remainder;

        return expected == Character.getNumericValue(digits.charAt(pos));
    }

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        String digits = cpf.replaceAll("\\D", "");

        if(digits.length() != 11) return false;
        if(digits.chars().distinct().count() == 1) return false;

        return verifyDigit(digits, 9) && verifyDigit(digits, 10);
    }
}
