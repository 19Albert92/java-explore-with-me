package ru.practicum.stat_server.exception;

public class DateInvalidateException extends RuntimeException {
    public DateInvalidateException(String message) {
        super(message);
    }
}
