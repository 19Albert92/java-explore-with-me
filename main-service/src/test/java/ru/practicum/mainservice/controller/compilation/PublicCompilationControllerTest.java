package ru.practicum.mainservice.controller.compilation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.service.compilation.CompilationService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCompilationController.class)
class PublicCompilationControllerTest {

    private static final String BASE_URL = "/compilations";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompilationService compilationService;

    @Test
    void shouldReturnListCompilation_whenMatchesToFilter() throws Exception {

        String expectedFirstCompilation = MockGeneratedData.generatorText(20);

        int from = 0;
        int size = 10;

        List<CompilationDto> compilations = List.of(
                CompilationDto.builder().title(expectedFirstCompilation).build(),
                CompilationDto.builder().title(MockGeneratedData.generatorText(37)).build(),
                CompilationDto.builder().title(MockGeneratedData.generatorText(41)).build()
        );

        when(compilationService.findAll(true, from, size)).thenReturn(compilations);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pinned", "true");
        params.add("from", Integer.toString(from));
        params.add("size", Integer.toString(size));

        mockMvc.perform(
                        get(BASE_URL)
                                .params(params)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(compilations.size()))
                .andExpect(jsonPath("$[0].title").value(expectedFirstCompilation));

        verify(compilationService).findAll(true, from, size);
    }

    @Test
    void shouldReturnCompilation_whenIfExists() throws Exception {

        long expectedId = 1L;
        String expectedTitle = MockGeneratedData.generatorText(30);

        when(compilationService.findById(expectedId)).thenReturn(CompilationDto.builder()
                .id(expectedId).title(expectedTitle).build());

        mockMvc.perform(
                        get(BASE_URL + "/{compId}", expectedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.title").value(expectedTitle));

        verify(compilationService).findById(expectedId);

    }
}