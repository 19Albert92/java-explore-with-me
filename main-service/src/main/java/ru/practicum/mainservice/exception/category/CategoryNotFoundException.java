package ru.practicum.mainservice.exception.category;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class CategoryNotFoundException extends BaseApplicationException {
    public CategoryNotFoundException(String reason, String message) {
        super(HttpStatus.NOT_FOUND, reason, message);
    }
}
