package dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import shared.dto.RequestFilterState;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RequestFilterStateTest {

    private Validator validator;

    private RequestFilterState requestFilterState;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        requestFilterState = RequestFilterState.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
    }

    @ParameterizedTest
    @NullSource
    public void validateField_start(LocalDateTime start) {
        String expectedErrorMessage = "Дата начало валидации обязательна";
        requestFilterState.setStart(start);
        assertThat(validator.validate(requestFilterState))
                .extracting(ConstraintViolation::getMessage)
                .contains(expectedErrorMessage);
    }

    @ParameterizedTest
    @NullSource
    public void validateField_end(LocalDateTime end) {
        String expectedErrorMessage = "Дата окончание валидации обязательна";
        requestFilterState.setEnd(end);
        assertThat(validator.validate(requestFilterState))
                .extracting(ConstraintViolation::getMessage)
                .contains(expectedErrorMessage);
    }
}