package ru.practicum.mainservice.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.service.category.CategoryService;
import ru.practicum.mainservice.service.event.EventToCategory;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCategoryController.class)
class AdminCategoryControllerTest {

    private static final String BASE_URL = "/admin/categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private EventToCategory categoryFacade;

    private static final long CAT_ID = 1L;

    @Test
    void shouldReturnStatus201AndNewCategory_whenSavingSuccessfully() throws Exception {

        String expectedName = "Test";

        NewCategoryDto newCategoryDto = new NewCategoryDto(expectedName);

        CategoryDto categoryDto = new CategoryDto(CAT_ID, expectedName);

        Mockito.when(categoryService.save(newCategoryDto)).thenReturn(categoryDto);

        mockMvc.perform(post(BASE_URL)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(expectedName));

        Mockito.verify(categoryService).save(newCategoryDto);
    }

    @Test
    void shouldReturnStatus204_whenDeleteSuccessfully() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/{catId}", CAT_ID))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryFacade).deleteOrExceptionCategoryById(CAT_ID);
    }

    @Test
    void shouldReturnStatus200AndNewCategory_whenUpdatedSuccessfully() throws Exception {

        String expectedName = "Test";

        NewCategoryDto newCategoryDto = new NewCategoryDto(expectedName);

        CategoryDto categoryDto = new CategoryDto(CAT_ID, expectedName);

        Mockito.when(categoryService.update(CAT_ID, newCategoryDto)).thenReturn(categoryDto);

        mockMvc.perform(patch(BASE_URL + "/{catId}", CAT_ID)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.id").value(CAT_ID));

        Mockito.verify(categoryService).update(CAT_ID, newCategoryDto);
    }

}