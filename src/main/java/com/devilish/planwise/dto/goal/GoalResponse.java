package com.devilish.planwise.dto.goal;

import com.devilish.planwise.entities.Goal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
    
    private Long id;
    private String description;
    private BigDecimal targetValue;
    private BigDecimal currentValue;
    private LocalDate deadline;
    private Goal.GoalStatus status;
    private LocalDateTime createdAt;
    private Boolean active;
    private BigDecimal progressPercentage;
    
    public static GoalResponse fromGoal(Goal goal) {
        return new GoalResponse(
            goal.getId(),
            goal.getDescription(),
            goal.getTargetValue(),
            goal.getCurrentValue(),
            goal.getDeadline(),
            goal.getStatus(),
            goal.getCreatedAt(),
            goal.getActive(),
            BigDecimal.valueOf(goal.getProgressPercentage())
        );
    }
}
