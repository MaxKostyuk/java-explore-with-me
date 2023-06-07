package ru.practicum.mainsvc.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService service;

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto category) {
        return service.addCategory(category);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable @Positive Long categoryId) {
        service.deleteCategory(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto category,
                                      @PathVariable @Positive Long categoryId) {
        return service.updateCategory(categoryId, category);
    }
}
