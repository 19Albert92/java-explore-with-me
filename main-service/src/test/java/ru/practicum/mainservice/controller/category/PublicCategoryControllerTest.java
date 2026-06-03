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
import ru.practicum.mainservice.service.category.CategoryService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCategoryController.class)
class PublicCategoryControllerTest {

    private static final String BASE_URL = "/categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private static final long CAT_ID = 1L;

    @Test
    void shouldReturnCategoriesListOrderId_whenIsNotEmpty() throws Exception {

        String expectedFirstName = "Мюзикл";

        List<CategoryDto> categories = List.of(
                CategoryDto.builder().name(expectedFirstName).id(CAT_ID).build(),
                CategoryDto.builder().name("Соревнование").build(),
                CategoryDto.builder().name("Пикник").build()
        );

        Mockito.when(categoryService.findAll(0, 10)).thenReturn(categories);

        mockMvc.perform(get(BASE_URL)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(CAT_ID))
                .andExpect(jsonPath("$[0].name").value(expectedFirstName));

        Mockito.verify(categoryService).findAll(0, 10);
    }

    @Test
    void shouldReturnCategory_whenIsExist() throws Exception {

        String expectedFirstName = "Мюзикл";

        Mockito.when(categoryService.findById(CAT_ID))
                .thenReturn(new CategoryDto(CAT_ID, expectedFirstName));

        mockMvc.perform(get(BASE_URL + "/{catId}", CAT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedFirstName))
                .andExpect(jsonPath("$.id").value(CAT_ID));

        Mockito.verify(categoryService).findById(CAT_ID);
    }
}