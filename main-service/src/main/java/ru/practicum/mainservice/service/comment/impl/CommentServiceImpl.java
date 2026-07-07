package ru.practicum.mainservice.service.comment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFilterParams;
import ru.practicum.mainservice.dto.comment.CreateCommentDto;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.comment.CommentNotFoundException;
import ru.practicum.mainservice.exception.comment.NotAuthorCommentException;
import ru.practicum.mainservice.mapper.CommentMapper;
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.repository.query.CommentQueryRepository;
import ru.practicum.mainservice.service.comment.CommentService;
import ru.practicum.mainservice.service.event.EventToRequestService;
import ru.practicum.mainservice.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventToRequestService eventService;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    @Transactional
    public CommentDto save(Long userId, Long eventId, CreateCommentDto createCommentDto) {

        User author = userService.findByIdOrException(userId);

        Event event = eventService.findEventByIdOrException(eventId);

        if (!event.hasEventState(EventState.PUBLISHED)) {

            log.error("Event with id {} is not published", eventId);

            throw new ConflictException(
                    "This operation is not available: Save",
                    "Cannot publish the comment because it's not in the right state: PUBLISHED"
            );
        }

        Comment comment = CommentMapper.toComment(createCommentDto, author, event);

        Comment newComment = commentRepository.save(comment);

        log.debug("Comment saved for comment id={}", newComment.getId());

        return CommentMapper.toCommentDto(newComment);
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, CreateCommentDto createCommentDto) {

        Comment findComment = findByIdOrThrowException(commentId);

        checkToCommentAuthor("Update", userId, findComment);

        User author = userService.findByIdOrException(userId);

        Comment comment = CommentMapper.toComment(createCommentDto, author, findComment.getEvent());

        Comment updatedComment = commentRepository.save(comment);

        log.debug("Comment updated for comment id={}", updatedComment.getId());

        return CommentMapper.toCommentDto(updatedComment);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long commentId) {

        Comment findComment = findByIdOrThrowException(commentId);

        if (userId != null) {
            checkToCommentAuthor("Delete", userId, findComment);
        }

        commentRepository.delete(findComment);

        log.debug("Comment deleted for comment id={}", commentId);
    }

    private void checkToCommentAuthor(String op, Long userId, Comment comment) {
        if (!comment.getUser().getId().equals(userId)) {
            log.error("Comment with id {} is not found", comment.getId());
            throw new NotAuthorCommentException(
                    "This operation is not available: " + op,
                    "You cannot edit this comment"
            );
        }
    }

    @Override
    public List<CommentDto> findAll(Long eventId, CommentFilterParams filterParams) {

        if (filterParams.getRangeStart() != null && filterParams.getRangeEnd() != null) {
            if (filterParams.getRangeStart().isAfter(filterParams.getRangeEnd())) {
                throw new ConflictException(
                        "Params conflict",
                        "Incorrectly filter params date"
                );
            }
        }

        return commentQueryRepository.findAllByFilter(eventId, filterParams).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    @Override
    public CommentDto findById(Long commentId) {
        return CommentMapper.toCommentDto(findByIdOrThrowException(commentId));
    }

    private Comment findByIdOrThrowException(Long commentId) throws CommentNotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(
                                "The required object was not found.",
                                String.format("Comment with id=%d was not found", commentId)
                        ));
    }
}
