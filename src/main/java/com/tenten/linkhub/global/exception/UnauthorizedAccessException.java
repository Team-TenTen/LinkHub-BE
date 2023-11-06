package com.tenten.linkhub.global.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
