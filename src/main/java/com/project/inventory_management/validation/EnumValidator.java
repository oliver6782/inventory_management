package com.project.inventory_management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        log.info("Checking if enum {} is valid", value);
        if (value == null) {
            return true; // Use @NotNull to validate null values
        }
        if (!Arrays.asList(enumClass.getEnumConstants()).contains(value)) {
            context.disableDefaultConstraintViolation();
            String message = String.format("value %s is not valid", value);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
