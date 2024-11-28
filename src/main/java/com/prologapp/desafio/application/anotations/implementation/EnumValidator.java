package com.prologapp.desafio.application.anotations.implementation;

import com.prologapp.desafio.application.anotations.EnumValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EnumValidator implements ConstraintValidator<EnumValidation, Enum<?>> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidation constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return enumClass.isInstance(value);
    }
}
