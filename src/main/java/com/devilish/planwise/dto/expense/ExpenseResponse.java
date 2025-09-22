package com.devilish.planwise.dto.expense;

import com.devilish.planwise.entities.Expense;
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
public class ExpenseResponse {
    
    private Long id;
    private String description;
    private BigDecimal value;
    private LocalDate date;
    private LocalDateTime createdAt;
    private Boolean active;
    private CategoryResponse category;
    
    public static ExpenseResponse fromExpense(Expense expense) {
        return new ExpenseResponse(
            expense.getId(),
            expense.getDescription(),
            expense.getValue(),
            expense.getDate(),
            expense.getCreatedAt(),
            expense.getActive(),
            CategoryResponse.fromCategory(expense.getCategory())
        );
    }
}
