package com.prologapp.desafio.application.anotations;

import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.application.anotations.implementation.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidation {
    Class<? extends Enum<?>> enumClass();
    String message() default ApplicationConstantes.VALOR_NAO_CORRESPONDE_OPCOES_VALIDAS;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

