package ru.practicum.mainservice.controller.compilation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationDto;
import ru.practicum.mainservice.service.compilation.CompilationService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCompilationController.class)
class AdminCompilationControllerTest {

    private static final String BASE_URL = "/admin/compilations";

    private static final String EXPECTED_TITLE = MockGeneratedData.generatorText(10);

    private static final Long EXPECTED_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompilationService compilationService;

    @Test
    void shouldReturnNewCompilation_whenCreatSuccessfully() throws Exception {

        NewCompilationDto newCompilationDto = NewCompilationDto.builder()
                .title(EXPECTED_TITLE)
                .build();

        CompilationDto compilationDto = CompilationDto.builder()
                .id(EXPECTED_ID)
                .title(EXPECTED_TITLE)
                .build();

        when(compilationService.save(newCompilationDto)).thenReturn(compilationDto);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newCompilationDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(EXPECTED_ID))
                .andExpect(jsonPath("$.title").value(EXPECTED_TITLE));

        verify(compilationService).save(newCompilationDto);
    }

    @Test
    void shouldReturnStatus204_whenDeleteSuccessfully() throws Exception {

        when(compilationService.findById(EXPECTED_ID)).thenReturn(CompilationDto.builder().build());

        mockMvc.perform(delete(BASE_URL + "/{compId}", EXPECTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());

        verify(compilationService).delete(EXPECTED_ID);
    }

    @Test
    void shouldReturnNewCompilation_whenUpdateSuccessfully() throws Exception {

        UpdateCompilationDto updateCompilationDto = UpdateCompilationDto.builder()
                .title(EXPECTED_TITLE)
                .build();

        CompilationDto compilationDto = CompilationDto.builder()
                .id(EXPECTED_ID)
                .title(EXPECTED_TITLE)
                .build();

        when(compilationService.update(EXPECTED_ID, updateCompilationDto)).thenReturn(compilationDto);

        when(compilationService.findById(EXPECTED_ID)).thenReturn(compilationDto);

        mockMvc.perform(
                        patch(BASE_URL + "/{compId}", EXPECTED_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(updateCompilationDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXPECTED_ID))
                .andExpect(jsonPath("$.title").value(EXPECTED_TITLE));

        verify(compilationService).update(EXPECTED_ID, updateCompilationDto);
    }
}