package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.entity.Category;

@UtilityClass
public class CategoryMapper {

    public static Category mapToCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.name())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category mapToCategory(Category category, NewCategoryDto categoryDto) {
        category.setName(categoryDto.name());
        return category;
    }
}
