package ru.practicum.mainservice.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFilterParams;
import ru.practicum.mainservice.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getAllComments(
            @Positive @PathVariable Long eventId,
            @Valid CommentFilterParams filterParams
    ) {
        return commentService.findAll(eventId, filterParams);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(
            @Positive @PathVariable Long commentId
    ) {
        return commentService.findById(commentId);
    }
}
