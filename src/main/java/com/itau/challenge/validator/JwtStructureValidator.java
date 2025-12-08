package com.itau.challenge.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtStructureValidator { @ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ResponseEntity<Map<String, Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {

    log.warn("Validation error: {}", ex.getMessage());

    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

    Map<String, Object> response = new HashMap<>();
    response.put("error", "Validation failed");
    response.put("details", errors);

    return ResponseEntity.badRequest().body(response);
}

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        log.warn("Illegal argument: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid request");
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        log.error("Unexpected error occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Internal server error");
        response.put("message", "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

