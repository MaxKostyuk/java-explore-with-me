package ru.practicum.mainsvc.category.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Category name should not be null or blank")
    @Size(min = 1, max = 50, message = "Category name length must be between 1 and 50 characters")
    private String name;
}
