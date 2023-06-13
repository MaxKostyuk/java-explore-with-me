package ru.practicum.mainsvc.category.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.exception.ElementNotFoundException;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final CategoryRepository categoryRepository;
    private static final String CATEGORY_NOT_FOUND_TEMPLATE = "Category with id %d not found";

    public CategoryRepositoryCustomImpl(@Lazy CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ElementNotFoundException(String.format(CATEGORY_NOT_FOUND_TEMPLATE, categoryId)));
    }
}
