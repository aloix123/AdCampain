package com.example.demo.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestControllerAdvicer {
    @ExceptionHandler(NoMoneyException.class)
    public ResponseEntity<String> handleNoMoneyException(NoMoneyException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Insufficient funds: " + ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Entity not found: " + ex.getMessage());
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEnumBindingErrors(HttpMessageNotReadableException ex) {
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
                    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
                }
            }
        }

        errors.put("error", "Malformed request");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
