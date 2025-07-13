package com.example.demo.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestControllerAdvicer {

    @ExceptionHandler(NoMoneyException.class)
    public ResponseEntity<ApiExceptionResponse> handleNoMoneyException(NoMoneyException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Insufficient funds: " + ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errors, request.getRequestURI());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Entity not found: " + ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, errors, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));

        ApiExceptionResponse errorResponse = new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionResponse> handleEnumBindingErrors(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidEx) {
            List<JsonMappingException.Reference> path = invalidEx.getPath();
            if (!path.isEmpty()) {
                String fieldName = path.get(0).getFieldName();
                Class<?> enumClass = invalidEx.getTargetType();

                if (enumClass.isEnum()) {
                    Object[] constants = enumClass.getEnumConstants();
                    String allowed = Arrays.stream(constants)
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));
                    errors.put(fieldName, "Invalid value. Allowed: " + allowed);
                    return buildErrorResponse(HttpStatus.BAD_REQUEST, errors, request.getRequestURI());
                }
            }
        }

        errors.put("error", "Malformed request");
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errors, request.getRequestURI());
    }

    // Generic fallback for all other exceptions, same ApiErrorResponse format
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Unexpected error: " + ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors, request.getRequestURI());
    }

    private ResponseEntity<ApiExceptionResponse> buildErrorResponse(HttpStatus status, Map<String, String> errors, String path) {
        ApiExceptionResponse errorResponse = new ApiExceptionResponse(
                status.value(),
                status.getReasonPhrase(),
                errors,
                path
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
