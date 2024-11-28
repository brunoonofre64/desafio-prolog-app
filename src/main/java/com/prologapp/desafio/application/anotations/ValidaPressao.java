package com.prologapp.desafio.application.anotations;

import com.prologapp.desafio.application.anotations.implementation.PressaoValidator;
import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PressaoValidator.class)
@Documented
public @interface ValidaPressao {
    String message() default ApplicationConstantes.PRESSAO_FORA_DE_PADRAO;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
