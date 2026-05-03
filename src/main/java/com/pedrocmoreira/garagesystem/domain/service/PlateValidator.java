package com.pedrocmoreira.garagesystem.domain.service;

import java.util.regex.Pattern;

public class PlateValidator {
    private PlateValidator() {}

    private static final Pattern EARLY_FORMAT   = Pattern.compile("^[A-Z]{3}[-]?\\d{4}$");
    private static final Pattern MERCOSUL_FORMAT = Pattern.compile("^[A-Z]{3}\\d[A-Z]\\d{2}$");

    public static boolean isValid(String placa) {
        if (placa == null) return false;
        String upper = placa.toUpperCase().trim();
        return EARLY_FORMAT.matcher(upper).matches()
                || MERCOSUL_FORMAT.matcher(upper).matches();
    }
}
