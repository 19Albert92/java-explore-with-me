package ru.practicum.mainservice.dto.category;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.practicum.mainservice.dto.UtilDtoValidate;

import java.util.stream.Stream;

class NewCategoryDtoTest extends UtilDtoValidate {

    private static Stream<Arguments> provideNameAndErrorText() {
        return Stream.of(
                Arguments.arguments(null, "Field: name. Error: must not be blank. Value: ''"),
                Arguments.arguments("", "Название категории должно иметь от 1 до 50 символов"),
                Arguments.arguments(" ", "Field: name. Error: must not be blank. Value: ''")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNameAndErrorText")
    void shouldFailNameValidation(String name, String error) {
        NewCategoryDto newCategoryDto = new NewCategoryDto(name);

        checkField(newCategoryDto, error);
    }
}