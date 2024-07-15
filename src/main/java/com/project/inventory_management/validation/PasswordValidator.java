package com.project.inventory_management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password cannot be null")
                    .addConstraintViolation();
            return false;
        }
        if (!pattern.matcher(password).matches()) {
            context.disableDefaultConstraintViolation();
            String message = """
                    Password must meet the following criteria:
                     - At least one digit [0-9]
                     - At least one lowercase letter [a-z]
                     - At least one uppercase letter [A-Z]
                     - At least one special character [@#$%^&+=]
                     - No whitespace allowed
                     - At least 8 characters long""";
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
