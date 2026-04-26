package com.pedrocmoreira.garagesystem.domain.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String message){
        super(message);
    }
}
