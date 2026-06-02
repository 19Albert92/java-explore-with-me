package ru.practicum.mainservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.mainservice.UtilConstant;
import ru.practicum.mainservice.dto.ApiException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BaseApplicationException.class)
    public ResponseEntity<ApiException> handleUserNotFoundException(BaseApplicationException e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(
                        ApiException.builder()
                                .status(e.getStatus())
                                .reason(e.getReason())
                                .message(e.getMessage())
                                .timestamp(LocalDateTime.now().format(UtilConstant.FORMATTER))
                                .build()
                );
    }
}
