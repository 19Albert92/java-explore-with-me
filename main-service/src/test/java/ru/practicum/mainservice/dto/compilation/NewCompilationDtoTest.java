package ru.practicum.mainservice.dto.compilation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.practicum.mainservice.dto.UtilDtoValidate;

import java.util.stream.Stream;

class NewCompilationDtoTest extends UtilDtoValidate {

    private static Stream<Arguments> provideTitleAndErrorsText() {
        return Stream.of(
                Arguments.arguments(null, "Field: title. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("", "Field: title. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments(" ", "Field: title. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("test".repeat(15), "Заголовок должен содержать не больше 50 символов")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTitleAndErrorsText")
    void shouldFailTitleValidation(String title, String error) {

        NewCompilationDto newCompilationDto = NewCompilationDto.builder().title(title).build();

        checkField(newCompilationDto, error);
    }
}