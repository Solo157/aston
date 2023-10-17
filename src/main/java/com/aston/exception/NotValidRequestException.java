package com.aston.exception;

public class NotValidRequestException extends RuntimeException {
    public NotValidRequestException(String message) {
        super(message);
    }
}
