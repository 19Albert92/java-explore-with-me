package ru.practicum.mainservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class UtilDtoValidate {

    protected Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    protected void checkField(Object newEventDto, String error) {
        assertThat(validator.validate(newEventDto))
                .extracting(ConstraintViolation::getMessage)
                .contains(error);
    }
}
