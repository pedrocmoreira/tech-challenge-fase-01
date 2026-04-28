package com.pedrocmoreira.garagesystem.domain.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Usuário ou senha incorretos");
    }
}
