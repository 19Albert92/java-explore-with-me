package ru.practicum.mainservice.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CreateCommentDto;
import ru.practicum.mainservice.service.comment.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody CreateCommentDto createCommentDto
    ) {
        return commentService.save(userId, eventId, createCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long commentId,
            @Valid @RequestBody CreateCommentDto createCommentDto
    ) {
        return commentService.update(userId, commentId, createCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long commentId
    ) {
        commentService.delete(userId, commentId);
    }
}
