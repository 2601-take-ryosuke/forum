package com.example.forum.validator;

import com.example.forum.validator.annotation.Content;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContentValidator implements ConstraintValidator<Content, String> {

    @Override
    public void initialize(Content content) {
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext con) {
        return !input.isBlank();
    }
}
