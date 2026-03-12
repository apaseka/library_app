package com.example.library.exception;

import com.example.library.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse error = getErrorResponse(httpStatus, ex, request);

        return ResponseEntity
                .status(httpStatus)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse error = getErrorResponse(httpStatus, ex, request);

        return ResponseEntity
                .status(httpStatus)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse error = getErrorResponse(httpStatus, ex, request, errors);

        return ResponseEntity
                .status(httpStatus)
                .body(error);
    }

    private static ErrorResponse getErrorResponse(HttpStatus httpStatus, RuntimeException ex,
                                                  HttpServletRequest request) {
        return getErrorResponse(
                httpStatus.name(),
                ex.getMessage(),
                request.getRequestURI(),
                Collections.emptyMap()
        );
    }

    private static ErrorResponse getErrorResponse(HttpStatus httpStatus, BindException ex,
                                                  HttpServletRequest request, Map<String, String> details) {
        return getErrorResponse(
                httpStatus.name(),
                ex.getMessage(),
                request.getRequestURI(),
                details
        );
    }

    private static ErrorResponse getErrorResponse(String error, String msg,
                                                  String uri, Map<String, String> details) {
        return new ErrorResponse(
                error,
                msg,
                Instant.now(),
                uri,
                details
        );
    }
}