package ru.practicum.mainservice.service.category.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.category.CategoryNotFoundException;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.service.category.CategoryService;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) throws ConflictException {

        checkCategoryByName(newCategoryDto.name());

        Category category = CategoryMapper.mapToCategory(newCategoryDto);

        category = categoryRepository.save(category);

        log.info("Category with name {} has been saved", category.getName());

        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public void delete(Long catId) {
        Category category = findByIdOrException(catId);

        categoryRepository.delete(category);

        log.debug("Category with id {} deleted", category.getId());
    }

    @Override
    public CategoryDto update(Long catId, NewCategoryDto newCategoryDto) {

        Category category = findByIdOrException(catId);

        if (category.getName().equals(newCategoryDto.name())) {
            return CategoryMapper.mapToCategoryDto(category);
        }

        checkCategoryByName(newCategoryDto.name());

        log.debug("Updating category with id {}", category.getId());

        if (newCategoryDto.name().equalsIgnoreCase(category.getName())) {
            return CategoryMapper.mapToCategoryDto(category);
        }

        Category updateCategory = CategoryMapper.mapToCategory(category, newCategoryDto);

        updateCategory = categoryRepository.save(updateCategory);

        return CategoryMapper.mapToCategoryDto(updateCategory);
    }

    private void checkCategoryByName(String name) {
        Category byName = categoryRepository.findByName(name);

        if (byName != null) {
            log.error("Category with name {} already exists", name);
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "Category name already exists"
            );
        }
    }

    @Override
    public List<CategoryDto> findAll(int from, int size) {

        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));

        log.info("Find all categories with page request {}", pageRequest);

        return categoryRepository.findAll(pageRequest).getContent().stream()
                .map(CategoryMapper::mapToCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long catId) {
        return CategoryMapper.mapToCategoryDto(findByIdOrException(catId));
    }

    @Override
    public Category findByIdOrException(Long catId) throws ConflictException {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "The required object was not found.",
                        "Category with id=%d was not found".formatted(catId)
                ));
    }
}
