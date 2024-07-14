package com.project.inventorymanagement.util;

import com.project.inventorymanagement.dto.ValidationErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValidationUtil {
    public ResponseEntity<ValidationErrorResponse> generateValidationErrorResponse(
            BindingResult bindingResult) {

        Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                        // under if (has error) statement, it's guaranteed to exist error,
                        // and message can't be null
                ));
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(errors);
        return ResponseEntity.badRequest().body(validationErrorResponse);

    }
}
