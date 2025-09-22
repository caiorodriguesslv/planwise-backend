package com.devilish.planwise.dto.income;

import com.devilish.planwise.entities.Income;
import com.devilish.planwise.dto.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeResponse {
    
    private Long id;
    private String description;
    private BigDecimal value;
    private LocalDate date;
    private LocalDateTime createdAt;
    private Boolean active;
    private CategoryResponse category;
    
    public static IncomeResponse fromIncome(Income income) {
        return new IncomeResponse(
            income.getId(),
            income.getDescription(),
            income.getValue(),
            income.getDate(),
            income.getCreatedAt(),
            income.getActive(),
            CategoryResponse.fromCategory(income.getCategory())
        );
    }
}
