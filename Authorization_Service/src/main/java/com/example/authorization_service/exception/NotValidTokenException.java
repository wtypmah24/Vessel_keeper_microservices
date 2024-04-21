package com.example.authorization_service.exception;

public class NotValidTokenException extends Exception {
    public NotValidTokenException(String message, Object... args) {
        super(String.format(message, args));
    }

}
