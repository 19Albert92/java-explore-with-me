package ru.practicum.mainservice.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class MinHoursFromNowValidator implements ConstraintValidator<MinHoursFromNow, LocalDateTime> {

    private int minHours;

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.isAfter(LocalDateTime.now().plusHours(minHours));
    }

    @Override
    public void initialize(MinHoursFromNow constraintAnnotation) {
        this.minHours = constraintAnnotation.hours();
    }
}
