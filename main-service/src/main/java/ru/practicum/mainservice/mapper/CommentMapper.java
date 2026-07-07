package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CreateCommentDto;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;

import java.time.LocalDateTime;

import static shared.UtilConstant.FORMATTER;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CreateCommentDto commentDto, User author, Event event) {
        return Comment.builder()
                .text(commentDto.text())
                .user(author)
                .event(event)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .createdAt(comment.getCreatedAt().format(FORMATTER))
                .build();
    }
}
