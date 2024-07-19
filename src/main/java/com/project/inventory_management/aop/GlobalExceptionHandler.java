package com.project.inventory_management.aop;

import com.project.inventory_management.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Aspect
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("path", request.getContextPath());
        errors.put("web request description", request.getDescription(false));
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Method argument not valid exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        errors.put("method", ex.getMethod());
        log.error("HTTP request method not supported exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("User not found exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MedicationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMedicationNotFoundException(
            MedicationNotFoundException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("Medication not found exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MedicationTypeNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMedicationTypeNotValidException(
            MedicationTypeNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("Medication type not valid exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserRoleNotValidException.class)
    public ResponseEntity<Map<String, String>> handleUserRoleNotValidException(
            UserRoleNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("Role type not valid exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Map<String, String>> handleUserExistException(
            UserExistException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("User already exist exception : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MedicationExistException.class)
    public ResponseEntity<Map<String, String>> handleMedicationExistException(
            MedicationExistException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("Medication already exists : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("Unauthorized : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientStockException(
            InsufficientStockException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        constructErrorMessages(ex, errors, request);
        log.error("Insufficient stock : {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    public void constructErrorMessages(Exception ex,
                                       Map<String, String> errors,
                                       WebRequest request) {
        errors.put("path", request.getContextPath());
        errors.put("web request description", request.getDescription(false));
        errors.put("exception", ex.getMessage());
    }
}
