package ru.practicum.mainservice.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CreateCommentDto;
import ru.practicum.mainservice.service.comment.CommentService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shared.UtilConstant.FORMATTER;

@WebMvcTest(PrivateCommentController.class)
class PrivateCommentControllerTest {

    private static final String BASE_URL = "/users/{userId}/comments";

    private static final long AUTHOR_ID = 1L;
    private static final long COMMENT_ID = 2L;
    private static final long EVENT_ID = 3L;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnNewComment_whenCreatedSuccessfully() throws Exception {

        String text = "New test comment";

        CreateCommentDto createCommentDto = new CreateCommentDto(text);

        CommentDto commentDto = CommentDto.builder()
                .id(COMMENT_ID)
                .createdAt(LocalDateTime.now().format(FORMATTER))
                .text(text)
                .eventId(EVENT_ID)
                .authorId(AUTHOR_ID)
                .build();

        when(commentService.save(AUTHOR_ID, EVENT_ID, createCommentDto)).thenReturn(commentDto);

        mockMvc.perform(
                        post(BASE_URL + "/events/{eventId}", AUTHOR_ID, EVENT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCommentDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(text));

        verify(commentService).save(AUTHOR_ID, EVENT_ID, createCommentDto);
    }

    @Test
    void shouldReturnUpdatedComment_whenUpdatedSuccessfully() throws Exception {

        String text = "Updated test comment";

        CreateCommentDto createCommentDto = new CreateCommentDto(text);

        CommentDto commentDto = CommentDto.builder()
                .id(COMMENT_ID)
                .createdAt(LocalDateTime.now().format(FORMATTER))
                .text(text)
                .eventId(EVENT_ID)
                .authorId(AUTHOR_ID)
                .build();

        when(commentService.update(AUTHOR_ID, COMMENT_ID, createCommentDto)).thenReturn(commentDto);

        mockMvc.perform(
                patch(BASE_URL + "/{commentId}",  AUTHOR_ID, COMMENT_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(createCommentDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(text));

        verify(commentService).update(AUTHOR_ID, COMMENT_ID, createCommentDto);
    }

    @Test
    void shouldReturnNoContent_whenCommentDeletedSuccessfully() throws Exception {

        mockMvc.perform(
                delete(BASE_URL + "/{commentId}",  AUTHOR_ID, COMMENT_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());

        verify(commentService).delete(AUTHOR_ID, COMMENT_ID);
    }
}