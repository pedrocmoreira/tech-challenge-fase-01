package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.domain.exception.InvalidCredentialsException;
import com.pedrocmoreira.garagesystem.domain.exception.InactiveUserException;
import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleInvalidCredentials(InvalidCredentialsException ex) {
        return errorBody(ex.getMessage());
    }

    @ExceptionHandler(InactiveUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleInactiveUser(InactiveUserException ex) {
        return errorBody(ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> handleInsufficientStock(InsufficientStockException ex) {
        return errorBody(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {
        return errorBody("Erro interno do servidor");
    }

    private Map<String, Object> errorBody(String message) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", message
        );
    }
}