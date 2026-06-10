package ru.practicum.mainservice.service.event;

import ru.practicum.mainservice.exception.ConflictException;

public interface EventToCategory {

    void deleteOrExceptionCategoryById(Long catId) throws ConflictException;
}
