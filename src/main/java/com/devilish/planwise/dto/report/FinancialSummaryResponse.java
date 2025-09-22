package com.devilish.planwise.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummaryResponse {
    
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalTransactions;
    private int incomeCount;
    private int expenseCount;
    
    public static FinancialSummaryResponse of(BigDecimal totalIncome, BigDecimal totalExpense, 
                                           LocalDate startDate, LocalDate endDate,
                                           int incomeCount, int expenseCount) {
        BigDecimal balance = totalIncome.subtract(totalExpense);
        int totalTransactions = incomeCount + expenseCount;
        
        return FinancialSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .startDate(startDate)
                .endDate(endDate)
                .totalTransactions(totalTransactions)
                .incomeCount(incomeCount)
                .expenseCount(expenseCount)
                .build();
    }
}
