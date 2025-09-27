package com.devilish.planwise.services.expense;

import com.devilish.planwise.dto.expense.ExpenseRequest;
import com.devilish.planwise.dto.expense.ExpenseResponse;
import com.devilish.planwise.entities.Category;
import com.devilish.planwise.entities.Expense;
import com.devilish.planwise.entities.User;
import com.devilish.planwise.repository.category.CategoryRepository;
import com.devilish.planwise.repository.expense.ExpenseRepository;
import com.devilish.planwise.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        
        // Verificar se a categoria existe e pertence ao usuário
        Category category = categoryRepository.findByIdAndUserIdAndActiveTrue(request.getCategoryId(), currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verificar se a categoria é do tipo DESPESA
        if (category.getType() != Category.CategoryType.DESPESA) {
            throw new RuntimeException("A categoria deve ser do tipo DESPESA");
        }

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setValue(request.getValue());
        expense.setDate(request.getDate());
        expense.setUser(currentUser);
        expense.setCategory(category);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setActive(true);

        Expense savedExpense = expenseRepository.save(expense);
        return ExpenseResponse.fromExpense(savedExpense);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getAllExpenses(Pageable pageable) {
        User currentUser = userService.getCurrentUserEntity();
        Page<Expense> expenses = expenseRepository.findByUserIdAndActiveTrueOrderByDateDesc(currentUser.getId(), pageable);
        return expenses.map(ExpenseResponse::fromExpense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {
        User currentUser = userService.getCurrentUserEntity();
        List<Expense> expenses = expenseRepository.findByUserIdAndActiveTrueOrderByDateDesc(currentUser.getId());
        return expenses.stream()
                .map(ExpenseResponse::fromExpense)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpensesByCategory(Long categoryId) {
        User currentUser = userService.getCurrentUserEntity();
        List<Expense> expenses = expenseRepository.findByUserIdAndCategoryIdAndActiveTrueOrderByDateDesc(currentUser.getId(), categoryId);
        return expenses.stream()
                .map(ExpenseResponse::fromExpense)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentUserEntity();
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetweenAndActiveTrueOrderByDateDesc(currentUser.getId(), startDate, endDate);
        return expenses.stream()
                .map(ExpenseResponse::fromExpense)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Expense expense = expenseRepository.findByIdAndUserIdAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        return ExpenseResponse.fromExpense(expense);
    }

    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Expense expense = expenseRepository.findByIdAndUserIdAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        // Verificar se a categoria existe e pertence ao usuário
        Category category = categoryRepository.findByIdAndUserIdAndActiveTrue(request.getCategoryId(), currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verificar se a categoria é do tipo DESPESA
        if (category.getType() != Category.CategoryType.DESPESA) {
            throw new RuntimeException("A categoria deve ser do tipo DESPESA");
        }

        expense.setDescription(request.getDescription());
        expense.setValue(request.getValue());
        expense.setDate(request.getDate());
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);
        return ExpenseResponse.fromExpense(savedExpense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Expense expense = expenseRepository.findByIdAndUserIdAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        // Soft delete
        expense.setActive(false);
        expenseRepository.save(expense);
    }

    public List<ExpenseResponse> searchExpenses(String search) {
        User currentUser = userService.getCurrentUserEntity();
        List<Expense> expenses = expenseRepository.findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(currentUser.getId(), search);
        return expenses.stream()
                .map(ExpenseResponse::fromExpense)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalExpense() {
        User currentUser = userService.getCurrentUserEntity();
        return expenseRepository.getTotalExpenseByUser(currentUser.getId());
    }

    public BigDecimal getTotalExpenseByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentUserEntity();
        return expenseRepository.getTotalExpenseByUserAndDateRange(currentUser.getId(), startDate, endDate);
    }
}
