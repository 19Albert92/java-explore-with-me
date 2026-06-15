package ru.practicum.mainservice.exception.comment;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.BaseApplicationException;

public class CommentNotFoundException extends BaseApplicationException {
  public CommentNotFoundException(String reason, String message) {
    super(HttpStatus.NOT_FOUND, reason, message);
  }
}
