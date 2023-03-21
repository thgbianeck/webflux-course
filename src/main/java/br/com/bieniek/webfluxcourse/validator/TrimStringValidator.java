package br.com.bieniek.webfluxcourse.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class TrimStringValidator implements ConstraintValidator<TrimString, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || value.trim().equals(value);
    }
}
