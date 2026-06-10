package ru.practicum.mainservice.dto.category;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class NewCategoryDtoTest {

    private Validator validator;

    private static Stream<Arguments> provideNameAndErrorText() {
        return Stream.of(
                Arguments.arguments(null, "Field: name. Error: must not be blank. Value: ''"),
                Arguments.arguments("", "Название категории должно иметь от 1 до 50 символов"),
                Arguments.arguments(" ", "Field: name. Error: must not be blank. Value: ''")
        );
    }

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator =  factory.getValidator();
        }
    }

    @ParameterizedTest
    @MethodSource("provideNameAndErrorText")
    void shouldFailNameValidation(String name, String error) {
        NewCategoryDto newCategoryDto = new NewCategoryDto(name);

        assertThat(validator.validate(newCategoryDto))
                .extracting(ConstraintViolation::getMessage)
                .contains(error);
    }
}