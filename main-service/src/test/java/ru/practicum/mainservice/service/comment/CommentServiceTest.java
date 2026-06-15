package ru.practicum.mainservice.service.comment;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.mainservice.MockGeneratedData;
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
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.repository.query.CommentQueryRepository;
import ru.practicum.mainservice.service.comment.impl.CommentServiceImpl;
import ru.practicum.mainservice.service.event.EventToRequestService;
import ru.practicum.mainservice.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static final Long AUTHOR_ID = 1L;
    private static final Long COMMENT_ID = 2L;
    private static final Long EVENT_ID = 3L;
    private static final String EXPECTED_TEXT = MockGeneratedData.generatorText(20);

    private static final User AUTHOR = User.builder().id(AUTHOR_ID).build();
    private static final Event CURRENT_EVENT = Event.builder()
            .id(EVENT_ID)
            .title(MockGeneratedData.generatorText(10))
            .state(EventState.PUBLISHED)
            .build();

    private static final Comment COMMENT = Comment.builder()
            .id(COMMENT_ID)
            .text(EXPECTED_TEXT)
            .user(AUTHOR)
            .event(CURRENT_EVENT)
            .createdAt(LocalDateTime.now())
            .build();

    private static final CreateCommentDto CREATE_COMMENT_DTO = CreateCommentDto.builder()
            .text(EXPECTED_TEXT)
            .build();

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentQueryRepository commentQueryRepository;

    @Mock
    private UserService userService;

    @Mock
    private EventToRequestService eventService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void shouldReturnedNewComment_whenSaveSuccessfully() {

        when(userService.findByIdOrException(anyLong())).thenReturn(AUTHOR);

        when(eventService.findEventByIdOrException(anyLong())).thenReturn(CURRENT_EVENT);

        when(commentRepository.save(any(Comment.class))).thenReturn(COMMENT);

        CommentDto savedCommentDto = commentService.save(AUTHOR_ID, EVENT_ID, CREATE_COMMENT_DTO);

        AssertionsForClassTypes.assertThat(savedCommentDto)
                .isNotNull()
                .isInstanceOf(CommentDto.class)
                .hasFieldOrPropertyWithValue("text", EXPECTED_TEXT);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void shouldReturnedConflictException_whenFromSaveEventNotPublished() {

        Event event = Event.builder()
                .id(EVENT_ID)
                .state(EventState.PENDING).build();

        when(userService.findByIdOrException(anyLong())).thenReturn(AUTHOR);

        when(eventService.findEventByIdOrException(anyLong())).thenReturn(event);

        Assertions.assertThrows(ConflictException.class, () ->
                commentService.save(AUTHOR_ID, EVENT_ID, CREATE_COMMENT_DTO));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void shouldReturnedUpdatedComment_whenUpdateSuccessfully() {

        when(userService.findByIdOrException(anyLong())).thenReturn(AUTHOR);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(COMMENT));

        when(commentRepository.save(any(Comment.class))).thenReturn(COMMENT);

        CommentDto updatedComment = commentService.update(AUTHOR_ID, COMMENT_ID, CREATE_COMMENT_DTO);

        AssertionsForClassTypes.assertThat(updatedComment)
                .isNotNull()
                .isInstanceOf(CommentDto.class)
                .hasFieldOrPropertyWithValue("text", EXPECTED_TEXT);
    }

    @Test
    void shouldReturnCommentNotFoundException_wheCommentNotFound() {

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(CommentNotFoundException.class, () ->
                        commentService.update(AUTHOR_ID, COMMENT_ID, CREATE_COMMENT_DTO),
                "Такого комментария нет"
        );

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void shouldReturnedNotAuthorCommentException_whenDeleteCommentNotAuthor() {

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(COMMENT));

        Assertions.assertThrows(NotAuthorCommentException.class,
                () -> commentService.delete(2L, COMMENT_ID),
                "Комментарий не пренадлежит данному пользователю"
        );

        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void shouldReturnedVoid_whenDeleteSuccessfully() {

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(COMMENT));

        commentService.delete(AUTHOR_ID, COMMENT_ID);

        verify(commentRepository).delete(any(Comment.class));
    }

    @Test
    void shouldReturnedAllComments_whenMatchingFilter() {

        List<Comment> comments = List.of(
                COMMENT,
                Comment.builder().text(MockGeneratedData.generatorText(10)).id(1L).event(CURRENT_EVENT).user(AUTHOR)
                        .createdAt(LocalDateTime.now().plusHours(2)).build(),
                Comment.builder().text(MockGeneratedData.generatorText(30)).id(2L).event(CURRENT_EVENT).user(AUTHOR)
                        .createdAt(LocalDateTime.now().plusHours(5)).build(),
                Comment.builder().text(MockGeneratedData.generatorText(19)).id(3L).event(CURRENT_EVENT).user(AUTHOR)
                        .createdAt(LocalDateTime.now().plusHours(1)).build()
        );

        when(commentQueryRepository.findAllByFilter(any(), any())).thenReturn(comments);

        CommentFilterParams filterParams = CommentFilterParams.builder().build();

        List<CommentDto> resultComments = commentService.findAll(EVENT_ID, filterParams);

        assertThat(resultComments)
                .isNotNull()
                .hasSize(comments.size())
                .first()
                .isInstanceOf(CommentDto.class)
                .hasFieldOrPropertyWithValue("text", COMMENT.getText())
                .hasFieldOrPropertyWithValue("id", COMMENT.getId());

        verify(commentQueryRepository).findAllByFilter(any(), any());
    }
}