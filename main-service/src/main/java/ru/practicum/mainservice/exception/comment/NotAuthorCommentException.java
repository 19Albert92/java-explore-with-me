package ru.practicum.mainservice.exception.comment;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class NotAuthorCommentException extends BaseApplicationException {
  public NotAuthorCommentException(String reason, String message) {
    super(HttpStatus.BAD_REQUEST, reason, message);
  }
}
