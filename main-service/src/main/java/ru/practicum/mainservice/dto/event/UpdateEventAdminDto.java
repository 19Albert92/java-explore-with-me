package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.entity.request.AdminStateAction;
import ru.practicum.mainservice.entity.event.Location;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.util.validation.MinHoursFromNow;

import java.time.LocalDateTime;

import static shared.UtilConstant.DATE_TIME_FORMAT;

@Builder
public record UpdateEventAdminDto(
        @Length(min = 20, max = 2000,
                message = "There should not be less than 20 and more than 2000 characters. Value: {validatedValue}")
        String annotation,
        Long category,
        @Length(min = 20, max = 7000,
                message = "There should not be less than 20 and more than 7000 characters. Value: {validatedValue}")
        String description,
        @MinHoursFromNow(message =
                "Field: eventDate. Error: must contain a date that has not yet arrived. Value: {validatedValue}")
        @JsonFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime eventDate,
        Location location,
        Boolean paid,
        @PositiveOrZero(message = "Field: participantLimit. Error: must not be null. Value: {validatedValue}")
        Integer participantLimit,
        Boolean requestModeration,
        @JsonProperty("stateAction")
        AdminStateAction state,
        @Length(min = 3, max = 120,
                message = "There should not be less than 3 and more than 120 characters. Value: {validatedValue}")
        String title
) {

        @JsonCreator
        public UpdateEventAdminDto {}

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

        public boolean hasState() {
                return state != null;
        }

        public EventState parseAdminState() {

                if (state == AdminStateAction.REJECT_EVENT) {
                        return EventState.REJECTED;
                }

                return EventState.PUBLISHED;
        }
}
