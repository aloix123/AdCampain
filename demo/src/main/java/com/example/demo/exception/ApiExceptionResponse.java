package com.example.demo.exception;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
@Getter
@Setter
public class ApiExceptionResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String, String> message;
    private String path;

    public ApiExceptionResponse(int status, String error, Map<String, String> message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }


}

