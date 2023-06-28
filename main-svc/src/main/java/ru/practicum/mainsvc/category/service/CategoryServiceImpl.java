package ru.practicum.mainsvc.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.category.mapper.CategoryMapper;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.exception.ElementNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryDto addCategory(CategoryDto category) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(category)));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        repository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto category) {
        category.setId(categoryId);
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(category)));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        return repository.findAll(PageRequest.of(from, size, Sort.by("id"))).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        return CategoryMapper.toCategoryDto(repository.findById(categoryId)
                .orElseThrow(() -> new ElementNotFoundException(String.format("Category with id=%d was not found", categoryId))));
    }
}
