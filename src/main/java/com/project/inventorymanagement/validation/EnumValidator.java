package com.project.inventorymanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull to validate null values
        }
        if (!Arrays.asList(enumClass.getEnumConstants()).contains(value)) {
            context.disableDefaultConstraintViolation();
            String message = String.format("Value %s is not valid", value);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        };
        return true;
    }
}
