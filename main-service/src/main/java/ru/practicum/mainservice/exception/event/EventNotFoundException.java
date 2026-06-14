package ru.practicum.mainservice.exception.event;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class EventNotFoundException extends BaseApplicationException {
    public EventNotFoundException(String reason, String message) {
        super(HttpStatus.NOT_FOUND, reason, message);
    }
}
