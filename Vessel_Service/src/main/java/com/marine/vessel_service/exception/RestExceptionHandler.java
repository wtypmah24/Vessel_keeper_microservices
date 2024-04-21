package com.marine.vessel_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {VesselException.class,})
    protected ResponseEntity<Object> handleIllegalState(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}