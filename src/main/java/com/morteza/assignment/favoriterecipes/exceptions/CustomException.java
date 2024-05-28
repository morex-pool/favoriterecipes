package com.morteza.assignment.favoriterecipes.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}