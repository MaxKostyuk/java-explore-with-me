package ru.practicum.mainsvc.category.repository;

import ru.practicum.mainsvc.category.model.Category;

public interface CategoryRepositoryCustom {

    Category getCategoryById(Long categoryId);
}
