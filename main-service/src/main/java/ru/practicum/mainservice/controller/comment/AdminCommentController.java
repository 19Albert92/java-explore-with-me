package ru.practicum.mainservice.controller.comment;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.service.comment.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments/{commentId}")
public class AdminCommentController {

    private final CommentService commentService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdminById(
            @Positive
            @PathVariable Long commentId
    ) {
        commentService.delete(null, commentId);
    }
}
