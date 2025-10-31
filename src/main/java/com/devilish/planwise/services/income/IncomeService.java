package com.devilish.planwise.services.income;

import com.devilish.planwise.dto.income.IncomeRequest;
import com.devilish.planwise.dto.income.IncomeResponse;
import com.devilish.planwise.entities.Category;
import com.devilish.planwise.entities.Income;
import com.devilish.planwise.entities.User;
import com.devilish.planwise.repository.category.CategoryRepository;
import com.devilish.planwise.repository.income.IncomeRepository;
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
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Transactional
    public IncomeResponse createIncome(IncomeRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        
        // Verificar se a categoria existe e pertence ao usuário
        Category category = categoryRepository.findByIdAndUserIdAndActiveTrue(request.getCategoryId(), currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verificar se a categoria é do tipo RECEITA
        if (category.getType() != Category.CategoryType.RECEITA) {
            throw new RuntimeException("A categoria deve ser do tipo RECEITA");
        }

        Income income = new Income();
        income.setDescription(request.getDescription());
        income.setValue(request.getValue());
        income.setDate(request.getDate());
        income.setUser(currentUser);
        income.setCategory(category);
        income.setCreatedAt(LocalDateTime.now());
        income.setActive(true);

        Income savedIncome = incomeRepository.save(income);
        return IncomeResponse.fromIncome(savedIncome);
    }

    @Transactional(readOnly = true)
    public Page<IncomeResponse> getAllIncomes(Pageable pageable) {
        User currentUser = userService.getCurrentUserEntity();
        Page<Income> incomes = incomeRepository.findByUserIdAndActiveTrueOrderByDateDesc(currentUser.getId(), pageable);
        // Força o carregamento da categoria antes de converter para DTO
        incomes.forEach(income -> income.getCategory().getName());
        return incomes.map(IncomeResponse::fromIncome);
    }

    @Transactional(readOnly = true)
    public List<IncomeResponse> getAllIncomes() {
        User currentUser = userService.getCurrentUserEntity();
        List<Income> incomes = incomeRepository.findByUserIdAndActiveTrueOrderByDateDesc(currentUser.getId());
        // Força o carregamento da categoria antes de converter para DTO
        incomes.forEach(income -> income.getCategory().getName());
        return incomes.stream()
                .map(IncomeResponse::fromIncome)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IncomeResponse> getIncomesByCategory(Long categoryId) {
        User currentUser = userService.getCurrentUserEntity();
        List<Income> incomes = incomeRepository.findByUserIdAndCategoryIdAndActiveTrueOrderByDateDesc(currentUser.getId(), categoryId);
        // Força o carregamento da categoria antes de converter para DTO
        incomes.forEach(income -> income.getCategory().getName());
        return incomes.stream()
                .map(IncomeResponse::fromIncome)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IncomeResponse> getIncomesByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentUserEntity();
        List<Income> incomes = incomeRepository.findByUserIdAndDateBetweenAndActiveTrueOrderByDateDesc(currentUser.getId(), startDate, endDate);
        // Força o carregamento da categoria antes de converter para DTO
        incomes.forEach(income -> income.getCategory().getName());
        return incomes.stream()
                .map(IncomeResponse::fromIncome)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IncomeResponse getIncomeById(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Income income = incomeRepository.findByIdAndUserIdAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        return IncomeResponse.fromIncome(income);
    }

    @Transactional
    public IncomeResponse updateIncome(Long id, IncomeRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Income income = incomeRepository.findByIdAndUserIdAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        // Verificar se a categoria existe e pertence ao usuário
        Category category = categoryRepository.findByIdAndUserIdAndActiveTrue(request.getCategoryId(), currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verificar se a categoria é do tipo RECEITA
        if (category.getType() != Category.CategoryType.RECEITA) {
            throw new RuntimeException("A categoria deve ser do tipo RECEITA");
        }

        income.setDescription(request.getDescription());
        income.setValue(request.getValue());
        income.setDate(request.getDate());
        income.setCategory(category);

        Income savedIncome = incomeRepository.save(income);
        return IncomeResponse.fromIncome(savedIncome);
    }

    @Transactional
    public void deleteIncome(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Income income = incomeRepository.findByIdAndUserIdAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        // Soft delete
        income.setActive(false);
        incomeRepository.save(income);
    }

    @Transactional(readOnly = true)
    public List<IncomeResponse> searchIncomes(String search) {
        User currentUser = userService.getCurrentUserEntity();
        List<Income> incomes = incomeRepository.findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(currentUser.getId(), search);
        // Força o carregamento da categoria antes de converter para DTO
        incomes.forEach(income -> income.getCategory().getName());
        return incomes.stream()
                .map(IncomeResponse::fromIncome)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome() {
        User currentUser = userService.getCurrentUserEntity();
        return incomeRepository.getTotalIncomeByUser(currentUser.getId());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncomeByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentUserEntity();
        return incomeRepository.getTotalIncomeByUserAndDateRange(currentUser.getId(), startDate, endDate);
    }
}
