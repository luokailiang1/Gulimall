package com.learning.gulimall.common.valid;

import jakarta.validation.ConstraintValidator;

import java.util.Arrays;

public class ListValueConstrainValidator implements ConstraintValidator<ListValue, Integer> {
    private int[] values;

    @Override
    public void initialize(ListValue constraintAnnotation) {
        // Initialization logic if needed
        this.values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(Integer value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (int v : values) {
            if (v == value) {
                return true;
            }
        }
        return false; // Value is not in the allowed list
    }
}
