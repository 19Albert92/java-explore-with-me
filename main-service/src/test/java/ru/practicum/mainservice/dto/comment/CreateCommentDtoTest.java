package ru.practicum.mainservice.dto.comment;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.practicum.mainservice.dto.UtilDtoValidate;

import java.util.stream.Stream;

class CreateCommentDtoTest extends UtilDtoValidate {

    private static Stream<Arguments> provideTextAndErrorText() {
        return Stream.of(
                Arguments.arguments(null, "The text of the comment should not be rustic."),
                Arguments.arguments("", "The text of the comment should not be rustic."),
                Arguments.arguments("er", "The text of the comment should be between 3 and 100 characters")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTextAndErrorText")
    void shouldFailedTextValidation(String text, String error) {
        CreateCommentDto dto = CreateCommentDto.builder().text(text).build();

        checkField(dto, error);
    }
}