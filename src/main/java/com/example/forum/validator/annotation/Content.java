package com.example.forum.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import com.example.forum.validator.ContentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = ContentValidator.class)
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Content {
    String message() default "Please input a text.";

    String fieldNameInErrorMessage();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
