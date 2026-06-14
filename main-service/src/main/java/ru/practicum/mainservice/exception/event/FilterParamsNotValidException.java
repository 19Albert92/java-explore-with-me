package ru.practicum.mainservice.exception.event;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class FilterParamsNotValidException extends BaseApplicationException {
    public FilterParamsNotValidException(String reason, String message) {
        super(HttpStatus.BAD_REQUEST, reason, message);
    }
}
