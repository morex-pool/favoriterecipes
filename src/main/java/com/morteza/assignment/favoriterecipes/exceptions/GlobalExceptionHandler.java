package com.morteza.assignment.favoriterecipes.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handler(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, System.currentTimeMillis());
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        body.put(ERROR, "Not Found!");
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handler(CustomException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, System.currentTimeMillis());
        body.put(STATUS, ex.getHttpStatus().value());
        body.put(ERROR, ex.getMessage());
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, request.getDescription(false));
        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handler(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, System.currentTimeMillis());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        // body.put(error, ex.getMessage()); // to avoid show long technical issue
        body.put(MESSAGE, "Validation failed!");
        body.put(PATH, request.getDescription(false));
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> body.put("validate field [" + e.getField() + "]", e.getDefaultMessage()));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handler(AuthenticationException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, System.currentTimeMillis());
        body.put(STATUS, HttpStatus.UNAUTHORIZED.value());
        body.put(ERROR, ex.getMessage());
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handler(Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String generalError = "Internal Server Error";
        boolean messageIsChanged = false;

        if (exceptionMessage.contains("could not execute statement")) {
            if (exceptionMessage.contains("Referential integrity constraint violation")) {
                exceptionMessage = "The record is in use and cannot be deleted!";
                httpStatus = HttpStatus.CONFLICT;
                generalError = "Internal data conflict happened!";
                messageIsChanged = true;
            }
        } else if (exceptionMessage.contains("Access is denied")) {
            exceptionMessage = "Access is denied!";
            httpStatus = HttpStatus.UNAUTHORIZED;
            generalError = "Unauthorized!";
            messageIsChanged = true;
        }

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, System.currentTimeMillis());
        body.put(STATUS, httpStatus.value());
        body.put(ERROR, generalError);
        body.put(MESSAGE, exceptionMessage);
        body.put(PATH, request.getDescription(false));

        if (!messageIsChanged) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(body, httpStatus);
    }

}
