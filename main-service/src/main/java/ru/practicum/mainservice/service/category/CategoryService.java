package ru.practicum.mainservice.service.category;

import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.exception.ConflictException;

import java.util.List;

public interface CategoryService {

    CategoryDto save(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(Long catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto findById(Long catId);

    Category findByIdOrException(Long catId) throws ConflictException;
}
