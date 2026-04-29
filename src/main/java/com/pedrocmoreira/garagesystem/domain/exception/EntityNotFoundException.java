package com.pedrocmoreira.garagesystem.domain.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, Object id) {
        super(String.format("%s não encontrado(a) com id: %s", entity, id));
    }
}
