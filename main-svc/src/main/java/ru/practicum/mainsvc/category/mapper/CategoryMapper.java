package ru.practicum.mainsvc.category.mapper;

import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.model.Category;

public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
