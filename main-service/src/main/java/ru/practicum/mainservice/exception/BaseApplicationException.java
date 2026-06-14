package ru.practicum.mainservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final String reason;

    public BaseApplicationException(HttpStatus status, String reason, String message) {
        super(message);
        this.status = status;
        this.reason = reason;
    }
}
