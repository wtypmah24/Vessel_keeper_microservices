package com.marine.voyage_service.exception;

public class VoyageException extends Exception{
    public VoyageException(String message, Object ...args) {
        super(String.format(message, args));
    }
}
