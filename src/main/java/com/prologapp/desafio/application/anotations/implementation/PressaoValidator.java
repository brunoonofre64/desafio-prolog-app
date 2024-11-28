package com.prologapp.desafio.application.anotations.implementation;

import com.prologapp.desafio.application.anotations.ValidaPressao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PressaoValidator implements ConstraintValidator<ValidaPressao, BigDecimal> {
    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String[] partes = value.toPlainString().split("\\.");

        if (partes[0].length() > 5) {
            return false;
        }

        if (partes.length > 1 && partes[1].length() > 2) {
            return false;
        }

        return true;
    }
}
