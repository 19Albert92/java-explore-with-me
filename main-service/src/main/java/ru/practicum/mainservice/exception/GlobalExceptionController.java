package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.mainservice.dto.ApiException;

import java.time.LocalDateTime;

import static shared.UtilConstant.FORMATTER;

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
                                .timestamp(LocalDateTime.now().format(FORMATTER))
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiException> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiException.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Failed to convert value of type java.lang.String to required type long; nested exception is java.lang.NumberFormatException")
                                .message(e.getMessage())
                                .timestamp(LocalDateTime.now().format(FORMATTER))
                                .build()
                );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiException> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiException.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Failed to convert value of type java.lang.String to required type long; nested exception is java.lang.NumberFormatException")
                                .message(e.getMessage())
                                .timestamp(LocalDateTime.now().format(FORMATTER))
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiException.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Incorrectly made request.")
                                .message(e.getMessage())
                                .timestamp(LocalDateTime.now().format(FORMATTER))
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException> handleException(Exception e) {

        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiException.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .reason("test")
                                .message(e.getMessage())
                                .timestamp(LocalDateTime.now().format(FORMATTER))
                                .build()
                );
    }
}
