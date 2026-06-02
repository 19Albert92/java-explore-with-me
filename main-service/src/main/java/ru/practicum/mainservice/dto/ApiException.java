package ru.practicum.mainservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApiException(
       List<String> errors,
       String message,
       String reason,
       HttpStatus status,
       String timestamp
) {
}
