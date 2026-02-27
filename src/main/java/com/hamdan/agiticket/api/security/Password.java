package com.hamdan.agiticket.api.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
public @interface Password {

    String message() default "A senha deve ter no mínimo 8 caracteres, sendo 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
