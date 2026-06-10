package ru.practicum.mainservice.exception.compilation;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class CompilationNotFoundException extends BaseApplicationException {
    public CompilationNotFoundException(String reason, String message) {
        super(HttpStatus.NOT_FOUND, reason, message);
    }
}
