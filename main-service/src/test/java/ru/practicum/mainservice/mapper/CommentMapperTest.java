package ru.practicum.mainservice.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CreateCommentDto;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static shared.UtilConstant.FORMATTER;

class CommentMapperTest {

    private static final String EXPECTED_TEXT = MockGeneratedData.generatorText(20);

    @Test
    void mapToCommentTest() {

        User user = new User();

        Event event = new Event();

        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .text(EXPECTED_TEXT)
                .build();

        Comment newComment = CommentMapper.toComment(createCommentDto, user, event);

        assertThat(newComment)
                .isNotNull()
                .isInstanceOf(Comment.class)
                .hasFieldOrPropertyWithValue("text", EXPECTED_TEXT);
    }

    @Test
    void mapToCommentDtoTest() {

        long expectedId = 1L;
        LocalDateTime expectedCreatedAt = LocalDateTime.now();

        User user = User.builder().id(1L).build();

        Event event = Event.builder().id(1L).build();

        Comment comment = Comment.builder()
                .id(expectedId)
                .text(EXPECTED_TEXT)
                .user(user)
                .event(event)
                .createdAt(expectedCreatedAt)
                .build();

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        assertThat(commentDto)
                .isNotNull()
                .isInstanceOf(CommentDto.class)
                .hasFieldOrPropertyWithValue("text", EXPECTED_TEXT)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt.format(FORMATTER));
    }
}