package com.example.crew_service.exception;

public class SeamanException extends Exception{
    public SeamanException(String message, Object ...args) {
        super(String.format(message, args));
    }
}
