package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.entity.event.Location;
import ru.practicum.mainservice.util.validation.MinHoursFromNow;

import java.time.LocalDateTime;

import static shared.UtilConstant.DATE_TIME_FORMAT;

@Builder
public record NewEventDto(
        @NotBlank(message = "Field: annotation. Error: must not be blank. Value: {validatedValue}")
        @Length(min = 20, max = 2000)
        String annotation,
        @Positive(message = "Field: category. Error: must not be blank. Value: {validatedValue}")
        long category,
        @NotBlank(message = "Field: description. Error: must not be blank. Value: {validatedValue}")
        @Length(min = 20, max = 7000)
        String description,
        @Future
        @JsonFormat(pattern = DATE_TIME_FORMAT)
        @NotNull(message = "Field: eventDate. Error: must not be null. Value: {validatedValue}")
        @MinHoursFromNow(message =
                "Field: eventDate. Error: must contain a date that has not yet arrived. Value: {validatedValue}")
        LocalDateTime eventDate,
        @NotNull(message = "Field: eventDate. Error: must not be null. Value: {validatedValue}")
        Location location,
        boolean paid,
        @PositiveOrZero(message = "Field: participantLimit. Error: must not be null. Value: {validatedValue}")
        int participantLimit,
        Boolean requestModeration,
        @NotBlank(message = "Field: title. Error: must not be blank. Value: {validatedValue}")
        @Length(min = 3, max = 120)
        String title
) {
}
