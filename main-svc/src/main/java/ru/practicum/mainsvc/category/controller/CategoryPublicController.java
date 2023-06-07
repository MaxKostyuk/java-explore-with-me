package ru.practicum.mainsvc.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.service.CategoryService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryPublicController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return service.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getById(@PathVariable @Positive Long categoryId) {
        return service.getById(categoryId);
    }
}
