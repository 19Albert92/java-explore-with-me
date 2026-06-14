package ru.practicum.mainservice.dto.event;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.practicum.mainservice.dto.UtilDtoValidate;

import java.time.LocalDateTime;
import java.util.stream.Stream;

class UpdateEventAdminDtoTest extends UtilDtoValidate {

    private static Stream<Arguments> provideAnnotationAndErrorsText() {
        return Stream.of(
                Arguments.arguments("test annotation",
                        "There should not be less than 20 and more than 2000 characters. Value: {validatedValue}"),
                Arguments.arguments("test annotation".repeat(500),
                        "There should not be less than 20 and more than 2000 characters. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideDescriptionAndErrorsText() {
        return Stream.of(
                Arguments.arguments("test annotation",
                        "There should not be less than 20 and more than 7000 characters. Value: {validatedValue}"),
                Arguments.arguments("test annotation".repeat(1000),
                        "There should not be less than 20 and more than 7000 characters. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideEventDateAndErrorsText() {

        return Stream.of(
                Arguments.arguments("2026-06-09T21:00:01",
                        "Field: eventDate. Error: must contain a date that has not yet arrived. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideTitleAndErrorsText() {
        return Stream.of(
                Arguments.arguments("te",
                        "There should not be less than 3 and more than 120 characters. Value: {validatedValue}"),
                Arguments.arguments("test annotation".repeat(10),
                        "There should not be less than 3 and more than 120 characters. Value: {validatedValue}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnnotationAndErrorsText")
    void shouldFailedAnnotationValidation(String annotation, String error) {

        UpdateEventAdminDto updateEventAdminDto = UpdateEventAdminDto.builder()
                .annotation(annotation)
                .build();

        checkField(updateEventAdminDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideDescriptionAndErrorsText")
    void shouldFailedDescriptionValidation(String description, String error) {

        UpdateEventAdminDto updateEventAdminDto = UpdateEventAdminDto.builder()
                .description(description)
                .build();

        checkField(updateEventAdminDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideEventDateAndErrorsText")
    void shouldFailedEventDateValidation(String eventDate, String error) {

        UpdateEventAdminDto updateEventAdminDto = UpdateEventAdminDto.builder()
                .eventDate(eventDate == null ? null : LocalDateTime.parse(eventDate))
                .build();

        checkField(updateEventAdminDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideTitleAndErrorsText")
    void shouldFailedTitleValidation(String title, String error) {

        UpdateEventAdminDto updateEventAdminDto = UpdateEventAdminDto.builder()
                .title(title)
                .build();

        checkField(updateEventAdminDto, error);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void shouldFailedParticipantLimitValidation(int limit) {

        UpdateEventAdminDto updateEventAdminDto = UpdateEventAdminDto.builder()
                .participantLimit(limit)
                .build();

        checkField(updateEventAdminDto,
                "Field: participantLimit. Error: must not be null. Value: {validatedValue}");
    }
}