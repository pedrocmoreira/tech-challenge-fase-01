package com.pedrocmoreira.garagesystem.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String plateName, int available, int requested) {
        super(String.format("Estoque insuficiente para '%s'. Disponível: %d, Solicitado: %d", plateName, available, requested));
    }
}

