package ru.practicum.mainservice.service.comment;

import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFilterParams;
import ru.practicum.mainservice.dto.comment.CreateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto save(Long userId, Long eventId, CreateCommentDto createCommentDto);

    CommentDto update(Long userId, Long commentId, CreateCommentDto createCommentDto);

    void delete(Long userId, Long commentId);

    List<CommentDto> findAll(Long eventId, CommentFilterParams filterParams);

    CommentDto findById(Long commentId);
}
