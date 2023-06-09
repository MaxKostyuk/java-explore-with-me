package ru.practicum.mainsvc.category.service;

import ru.practicum.mainsvc.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto category);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, CategoryDto category);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getById(Long categoryId);
}
