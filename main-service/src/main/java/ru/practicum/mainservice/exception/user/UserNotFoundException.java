package ru.practicum.mainservice.exception.user;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class UserNotFoundException extends BaseApplicationException {
    public UserNotFoundException(String reason, String message) {
        super(HttpStatus.NOT_FOUND, reason, message);
    }
}
