package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseApplicationException {
    public ConflictException(String reason, String message) {
        super(HttpStatus.CONFLICT, reason, message);
    }
}
