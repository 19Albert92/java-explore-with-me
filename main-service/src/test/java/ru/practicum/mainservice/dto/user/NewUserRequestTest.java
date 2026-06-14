package ru.practicum.mainservice.dto.user;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.practicum.mainservice.dto.UtilDtoValidate;

import java.util.stream.Stream;

class NewUserRequestTest extends UtilDtoValidate {

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

    @ParameterizedTest
    @MethodSource("provideEmailsAndErrorsText")
    void shouldFailEmailValidation(String email, String error) {

        NewUserRequest newUserRequest = NewUserRequest.builder()
                .email(email)
                .name("Petia")
                .build();

        checkField(newUserRequest, error);
    }

    @ParameterizedTest
    @MethodSource("provideNameAndErrorsText")
    void shouldFailNameValidation(String name, String error) {
        NewUserRequest newUserRequest = NewUserRequest.builder()
                .email("example@google.com")
                .name(name)
                .build();

        checkField(newUserRequest, error);
    }
}