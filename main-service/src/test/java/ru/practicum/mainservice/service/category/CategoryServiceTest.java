package ru.practicum.mainservice.service.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.exception.category.CategoryNotFoundException;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.service.category.impl.CategoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final String NEW_CATEGORY_NAME = "Соревнование";

    private static final String OLD_CATEGORY_NAME = "Пикник";

    @Test
    void shouldReturnCategory_whenCreatingNewCategorySuccessfully() {

        NewCategoryDto categoryDto = new NewCategoryDto(NEW_CATEGORY_NAME);

        Category category = Category.builder().name(NEW_CATEGORY_NAME).id(1L).build();

        Mockito.when(categoryRepository.findByName(NEW_CATEGORY_NAME)).thenReturn(null);

        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        CategoryDto newCategory = categoryService.save(categoryDto);

        assertThat(newCategory)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", NEW_CATEGORY_NAME);

        Mockito.verify(categoryRepository).findByName(NEW_CATEGORY_NAME);
        Mockito.verify(categoryRepository).save(Mockito.any(Category.class));
    }

    @Test
    void shouldReturnNotFoundException_whenCategoryDoesNotExist() {

        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () ->
                categoryService.delete(1L));

        Mockito.verify(categoryRepository).findById(Mockito.anyLong());

        Mockito.verify(categoryRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void shouldUpdateCategory_whenUpdatingCategorySuccessfully() {

        NewCategoryDto categoryDto = new NewCategoryDto(NEW_CATEGORY_NAME);

        Category category = Category.builder().name(OLD_CATEGORY_NAME).id(1L).build();

        Mockito.when(categoryRepository.findByName(NEW_CATEGORY_NAME)).thenReturn(null);

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        CategoryDto newCategory = categoryService.update(1L, categoryDto);

        assertThat(newCategory)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", NEW_CATEGORY_NAME);

        Mockito.verify(categoryRepository).findByName(NEW_CATEGORY_NAME);
        Mockito.verify(categoryRepository).findById(1L);
        Mockito.verify(categoryRepository).save(Mockito.any(Category.class));
    }

    @Test
    void shouldReturnListCategories() {

        Page<Category> categoriesPage = new PageImpl<>(List.of(
                Category.builder().id(1L).name(NEW_CATEGORY_NAME).build(),
                Category.builder().id(2L).name(OLD_CATEGORY_NAME).build()
        ));

        Mockito.when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoriesPage);

        List<CategoryDto> resultCategories = categoryService.findAll(0, 10);

        assertThat(resultCategories)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("name", NEW_CATEGORY_NAME);

        Mockito.verify(categoryRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnCategoryNotFoundException_whenUserDoesNotExist() {

        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));

        Mockito.verify(categoryRepository).findById(1L);
    }
}