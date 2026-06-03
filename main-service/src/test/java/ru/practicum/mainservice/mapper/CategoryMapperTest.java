package ru.practicum.mainservice.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.entity.Category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CategoryMapperTest {

    private static final String NAME = "Соревнование";
    private static final long ID = 10;

    @Test
    void mapToCategoryTest() {

        NewCategoryDto newCategoryDto = new NewCategoryDto(NAME);

        Category category = CategoryMapper.mapToCategory(newCategoryDto);

        assertThat(category)
                .isNotNull()
                .isInstanceOf(Category.class)
                .hasFieldOrPropertyWithValue("name", NAME);
    }

    @Test
    void mapToCategoryDtoTest() {

        Category category = new Category(ID, NAME);

        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);

        assertThat(categoryDto)
                .isNotNull()
                .isInstanceOf(CategoryDto.class)
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("name", NAME);
    }
}