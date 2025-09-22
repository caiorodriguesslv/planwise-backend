package com.devilish.planwise.dto.category;

import com.devilish.planwise.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    
    private Long id;
    private String name;
    private Category.CategoryType type;
    private LocalDateTime createdAt;
    private Boolean active;
    
    public static CategoryResponse fromCategory(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getType(),
            category.getCreatedAt(),
            category.getActive()
        );
    }
}
