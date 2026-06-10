package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.entity.event.Location;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.entity.event.UpdateEventState;
import ru.practicum.mainservice.util.validation.MinHoursFromNow;

import java.time.LocalDateTime;

import static shared.UtilConstant.DATE_TIME_FORMAT;

@Builder
public record UpdateEventDto(
        @Length(min = 20, max = 2000)
        String annotation,
        Long category,
        @Length(min = 20, max = 7000)
        String description,
        @Future
        @JsonFormat(pattern = DATE_TIME_FORMAT)
        @MinHoursFromNow(message =
                "Field: eventDate. Error: must contain a date that has not yet arrived. Value: {validatedValue}")
        LocalDateTime eventDate,
        Location location,
        Boolean paid,
        @PositiveOrZero(message = "Field: participantLimit. Error: must not be null. Value: {validatedValue}")
        Integer participantLimit,
        Boolean requestModeration,
        @Length(min = 3, max = 120)
        String title,
        UpdateEventState stateAction
) {

        public boolean hasAnnotation() {
                return annotation != null;
        }

        public boolean hasCategory() {
                return category != null;
        }

        public boolean hasDescription() {
                return description != null;
        }

        public boolean hasEventDate() {
                return eventDate != null;
        }

        public boolean hasLocation() {
                return location != null;
        }

        public boolean hasPaid() {
                return paid != null;
        }

        public boolean hasParticipantLimit() {
                return participantLimit != null;
        }

        public boolean hasRequestModeration() {
                return requestModeration != null;
        }

        public boolean hasTitle() {
                return title != null;
        }

        public boolean hasStateAction() {
                return stateAction != null;
        }

        public EventState parseUpdatedState() {
                if (stateAction == UpdateEventState.SEND_TO_REVIEW) {
                        return EventState.PENDING;
                }

                return EventState.CANCELED;
        }
}
