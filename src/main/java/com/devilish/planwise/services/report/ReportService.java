package com.devilish.planwise.services.report;

import com.devilish.planwise.dto.report.FinancialSummaryResponse;
import com.devilish.planwise.repository.income.IncomeRepository;
import com.devilish.planwise.repository.expense.ExpenseRepository;
import com.devilish.planwise.repository.goal.GoalRepository;
import com.devilish.planwise.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public FinancialSummaryResponse getFinancialSummary() {
        Long userId = userService.getCurrentUserEntity().getId();
        
        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUser(userId);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUser(userId);
        
        // Contar transações
        long incomeCount = incomeRepository.countByUserIdAndActiveTrue(userId);
        long expenseCount = expenseRepository.countByUserIdAndActiveTrue(userId);
        
        return FinancialSummaryResponse.of(
            totalIncome != null ? totalIncome : BigDecimal.ZERO,
            totalExpense != null ? totalExpense : BigDecimal.ZERO,
            null, // startDate
            null, // endDate
            (int) incomeCount,
            (int) expenseCount
        );
    }

    @Transactional(readOnly = true)
    public FinancialSummaryResponse getFinancialSummaryByDateRange(LocalDate startDate, LocalDate endDate) {
        Long userId = userService.getCurrentUserEntity().getId();
        
        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUserAndDateRange(userId, startDate, endDate);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUserAndDateRange(userId, startDate, endDate);
        
        // Contar transações no período
        long incomeCount = incomeRepository.countByUserIdAndDateBetweenAndActiveTrueOrderByDateDesc(userId, startDate, endDate);
        long expenseCount = expenseRepository.countByUserIdAndDateBetweenAndActiveTrueOrderByDateDesc(userId, startDate, endDate);
        
        return FinancialSummaryResponse.of(
            totalIncome != null ? totalIncome : BigDecimal.ZERO,
            totalExpense != null ? totalExpense : BigDecimal.ZERO,
            startDate,
            endDate,
            (int) incomeCount,
            (int) expenseCount
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getGoalsSummary() {
        Long userId = userService.getCurrentUserEntity().getId();
        
        Map<String, Object> summary = new HashMap<>();
        
        // Contar metas por status
        summary.put("totalGoals", goalRepository.countByUserIdAndActiveTrue(userId));
        summary.put("goalsInProgress", goalRepository.countByUserAndStatusAndActiveTrue(userId, com.devilish.planwise.entities.Goal.GoalStatus.EM_ANDAMENTO));
        summary.put("goalsAchieved", goalRepository.countByUserAndStatusAndActiveTrue(userId, com.devilish.planwise.entities.Goal.GoalStatus.ATINGIDA));
        summary.put("goalsExpired", goalRepository.countByUserAndStatusAndActiveTrue(userId, com.devilish.planwise.entities.Goal.GoalStatus.VENCIDA));
        
        return summary;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlySummary(int year, int month) {
        Long userId = userService.getCurrentUserEntity().getId();
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUserAndDateRange(userId, startDate, endDate);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUserAndDateRange(userId, startDate, endDate);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("year", year);
        summary.put("month", month);
        summary.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        summary.put("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
        summary.put("balance", (totalIncome != null ? totalIncome : BigDecimal.ZERO)
                .subtract(totalExpense != null ? totalExpense : BigDecimal.ZERO));
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        
        return summary;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getYearlySummary(int year) {
        Long userId = userService.getCurrentUserEntity().getId();
        
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUserAndDateRange(userId, startDate, endDate);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUserAndDateRange(userId, startDate, endDate);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("year", year);
        summary.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        summary.put("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
        summary.put("balance", (totalIncome != null ? totalIncome : BigDecimal.ZERO)
                .subtract(totalExpense != null ? totalExpense : BigDecimal.ZERO));
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        
        return summary;
    }
}
