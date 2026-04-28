package com.pedrocmoreira.garagesystem.domain.exception;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException(String username) {
        super(String.format("Usuário '%s' está inativo", username));
    }
}