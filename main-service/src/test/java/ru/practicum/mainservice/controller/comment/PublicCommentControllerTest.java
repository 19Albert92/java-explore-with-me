package ru.practicum.mainservice.controller.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentFilterParams;
import ru.practicum.mainservice.service.comment.CommentService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCommentController.class)
class PublicCommentControllerTest {

    private static final String BASE_URL = "/comments";

    private static final long COMMENT_ID = 2L;
    private static final long EVENT_ID = 3L;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnedCommentList_whenCommentMatcherByFilterParams() throws Exception {

        CommentFilterParams filterParams = CommentFilterParams.builder()
                .userIds(Set.of(1L, 2L))
                .from(0)
                .size(10)
                .build();

        List<CommentDto> commentList = List.of(
                CommentDto.builder().id(1L).text(MockGeneratedData.generatorText(10)).build(),
                CommentDto.builder().id(2L).text(MockGeneratedData.generatorText(15)).build(),
                CommentDto.builder().id(3L).text(MockGeneratedData.generatorText(28)).build()
        );

        when(commentService.findAll(EVENT_ID, filterParams)).thenReturn(commentList);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userIds", "1,2");
        params.add("from", "0");
        params.add("size", "10");

        mockMvc.perform(
                        get(BASE_URL + "/events/{eventId}", EVENT_ID)
                                .params(params)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(commentList.size()));

        verify(commentService).findAll(EVENT_ID, filterParams);
    }

    @Test
    void shouldReturnComment_whenIfNotEmpty() throws Exception {

        String text = MockGeneratedData.generatorText(20);

        CommentDto commentDto = CommentDto.builder()
                .id(COMMENT_ID)
                .text(text)
                .build();

        when(commentService.findById(COMMENT_ID)).thenReturn(commentDto);

        mockMvc.perform(
                        get(BASE_URL + "/{commentId}", COMMENT_ID)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(text));

        verify(commentService).findById(COMMENT_ID);
    }
}