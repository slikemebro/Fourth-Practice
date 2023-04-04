package com.ua.glebkorobov.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class ValidateDto {
    private static Validator validator;

    public ValidateDto() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Set<ConstraintViolation<Product>> getValidate(Product product) {
        return validator.validate(product);
    }
}
