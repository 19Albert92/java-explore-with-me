package ru.practicum.mainservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.mainservice.entity.Category;

import java.util.List;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldReturnBooleanValue_whenCategoryExistsByName() {

        String expectedName = "Соревнование";

        List<Category> categories = List.of(
                Category.builder().name(expectedName).build(),
                Category.builder().name("Пикник").build()
        );

        categoryRepository.saveAll(categories);

        Assertions.assertTrue(categoryRepository.existsByName(expectedName),
                "Такая категория существует");

        Assertions.assertFalse(categoryRepository.existsByName("Плавание"),
                "Такой категории не существует");
    }
}