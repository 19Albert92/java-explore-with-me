package ru.practicum.mainservice.service.event.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.service.category.CategoryService;
import ru.practicum.mainservice.service.event.EventToCategory;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventToCategoryImpl implements EventToCategory {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    @Override
    @Transactional
    public void deleteOrExceptionCategoryById(Long catId) throws ConflictException {

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "Category name already exists"
            );
        }

        categoryService.delete(catId);
    }
}
