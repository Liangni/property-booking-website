package com.penny.annotation;

import com.penny.validator.PublishPropertyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PublishPropertyValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublishPropertyCheck {
    String message() default "Property detail is not correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
