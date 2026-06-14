package ru.practicum.mainservice.dto.event;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.practicum.mainservice.dto.UtilDtoValidate;
import ru.practicum.mainservice.entity.event.Location;

import java.time.LocalDateTime;
import java.util.stream.Stream;

class NewEventDtoTest extends UtilDtoValidate {

    private static Stream<Arguments> provideAnnotationAndErrorsText() {
        return Stream.of(
                Arguments.arguments(null, "Field: annotation. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("", "Field: annotation. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("test annotation",
                        "There should not be less than 20 and more than 2000 characters. Value: {validatedValue}"),
                Arguments.arguments("test annotation".repeat(500),
                        "There should not be less than 20 and more than 2000 characters. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideTitleAndErrorsText() {
        return Stream.of(
                Arguments.arguments(null, "Field: title. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("", "Field: title. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("te",
                        "There should not be less than 3 and more than 120 characters. Value: {validatedValue}"),
                Arguments.arguments("test annotation".repeat(10),
                        "There should not be less than 3 and more than 120 characters. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideDescriptionAndErrorsText() {
        return Stream.of(
                Arguments.arguments(null, "Field: description. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("", "Field: description. Error: must not be blank. Value: {validatedValue}"),
                Arguments.arguments("test annotation",
                        "There should not be less than 20 and more than 7000 characters. Value: {validatedValue}"),
                Arguments.arguments("test annotation".repeat(1000),
                        "There should not be less than 20 and more than 7000 characters. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideCategoryAndErrorsText() {
        return Stream.of(
                Arguments.arguments(0, "Field: category. Error: must be positive. Value: {validatedValue}"),
                Arguments.arguments(-1, "Field: category. Error: must be positive. Value: {validatedValue}")
        );
    }

    private static Stream<Arguments> provideEventDateAndErrorsText() {

        return Stream.of(
                Arguments.arguments(null, "Field: eventDate. Error: must not be null. Value: {validatedValue}"),
                Arguments.arguments("2024-12-31T15:10:05",
                        "Field: eventDate. Error: must be future. Value: {validatedValue}"),
                Arguments.arguments("2026-06-09T21:00:01",
                        "Field: eventDate. Error: must contain a date that has not yet arrived. Value: {validatedValue}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnnotationAndErrorsText")
    void shouldFailedAnnotationValidation(String annotation, String error) {

        NewEventDto newEventDto = NewEventDto.builder()
                .annotation(annotation)
                .build();

        checkField(newEventDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideCategoryAndErrorsText")
    void shouldFailedCategoryValidation(long category, String error) {

        NewEventDto newEventDto = NewEventDto.builder()
                .category(category)
                .build();

        checkField(newEventDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideDescriptionAndErrorsText")
    void shouldFailedDescriptionValidation(String description, String error) {

        NewEventDto newEventDto = NewEventDto.builder()
                .description(description)
                .build();

        checkField(newEventDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideEventDateAndErrorsText")
    void shouldFailedEventDateValidation(String eventDate, String error) {

        NewEventDto newEventDto = NewEventDto.builder()
                .eventDate(eventDate == null ? null : LocalDateTime.parse(eventDate))
                .build();

        checkField(newEventDto, error);
    }

    @ParameterizedTest
    @MethodSource("provideTitleAndErrorsText")
    void shouldFailedTitleValidation(String title, String error) {

        NewEventDto newEventDto = NewEventDto.builder()
                .title(title)
                .build();

        checkField(newEventDto, error);
    }

    @ParameterizedTest
    @NullSource
    void shouldFailedLocationValidation(Location location) {

        NewEventDto newEventDto = NewEventDto.builder()
                .location(location)
                .build();

        checkField(newEventDto, "Field: eventDate. Error: must not be null. Value: {validatedValue}");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void shouldFailedParticipantLimitValidation(int limit) {

        NewEventDto newEventDto = NewEventDto.builder()
                .participantLimit(limit)
                .build();

        checkField(newEventDto, "Field: participantLimit. Error: must not be null. Value: {validatedValue}");
    }
}