package ru.practicum.mainservice.dto.user;

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

class NewUserRequestTest {

    private Validator validator;

    private static Stream<Arguments> provideEmailsAndErrorsText() {
        return Stream.of(
                Arguments.arguments(null, "Field: email. Error: must not be blank. Value: null"),
                Arguments.arguments("", "Email должен состоять из 6 - 254 символов"),
                Arguments.arguments("example.email", "Email не валиден")
        );
    }

    private static Stream<Arguments> provideNameAndErrorsText() {
        return Stream.of(
                Arguments.arguments(null, "Field: name. Error: must not be blank. Value: null"),
                Arguments.arguments("", "Имя должен состоять из 6 - 254 символов"),
                Arguments.arguments(" ", "Имя должен состоять из 6 - 254 символов")
        );
    }

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @ParameterizedTest
    @MethodSource("provideEmailsAndErrorsText")
    void shouldFailEmailValidation(String email, String error) {

        NewUserRequest newUserRequest = NewUserRequest.builder()
                .email(email)
                .name("Petia")
                .build();

        assertThat(validator.validate(newUserRequest))
                .extracting(ConstraintViolation::getMessage)
                .contains(error);
    }

    @ParameterizedTest
    @MethodSource("provideNameAndErrorsText")
    void shouldFailNameValidation(String name, String error) {
        NewUserRequest newUserRequest = NewUserRequest.builder()
                .email("example@google.com")
                .name(name)
                .build();

        assertThat(validator.validate(newUserRequest))
                .extracting(ConstraintViolation::getMessage)
                .contains(error);
    }
}