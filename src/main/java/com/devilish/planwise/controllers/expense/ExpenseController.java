package com.devilish.planwise.controllers.expense;

import com.devilish.planwise.dto.expense.ExpenseRequest;
import com.devilish.planwise.dto.expense.ExpenseResponse;
import com.devilish.planwise.services.expense.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        try {
            log.info("Criando despesa: {}", request);
            ExpenseResponse response = expenseService.createExpense(request);
            log.info("Despesa criada com sucesso: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Erro ao criar despesa: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(Pageable pageable) {
        try {
            log.info("Buscando todas as despesas com paginação: {}", pageable);
            Page<ExpenseResponse> expenses = expenseService.getAllExpenses(pageable);
            log.info("Encontradas {} despesas", expenses.getTotalElements());
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Erro ao buscar despesas paginadas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseResponse>> getAllExpensesList() {
        try {
            log.info("Buscando todas as despesas como lista");
            List<ExpenseResponse> expenses = expenseService.getAllExpenses();
            log.info("Encontradas {} despesas", expenses.size());
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Erro ao buscar todas as despesas: ", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Error-Message", "Erro interno: " + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(@PathVariable Long categoryId) {
        try {
            log.info("Buscando despesas da categoria: {}", categoryId);
            List<ExpenseResponse> expenses = expenseService.getExpensesByCategory(categoryId);
            log.info("Encontradas {} despesas na categoria {}", expenses.size(), categoryId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Erro ao buscar despesas por categoria {}: ", categoryId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Buscando despesas entre {} e {}", startDate, endDate);
            List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
            log.info("Encontradas {} despesas no período", expenses.size());
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Erro ao buscar despesas por período: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        try {
            log.info("Buscando despesa com ID: {}", id);
            ExpenseResponse expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            log.warn("Despesa não encontrada com ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro ao buscar despesa por ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        try {
            log.info("Atualizando despesa {}: {}", id, request);
            ExpenseResponse expense = expenseService.updateExpense(id, request);
            log.info("Despesa {} atualizada com sucesso", id);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar despesa {}: ", id, e);
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("deve ser do tipo")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Erro interno ao atualizar despesa {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        try {
            log.info("Deletando despesa com ID: {}", id);
            expenseService.deleteExpense(id);
            log.info("Despesa {} deletada com sucesso", id);
            return ResponseEntity.ok("Despesa deletada com sucesso");
        } catch (RuntimeException e) {
            log.warn("Tentativa de deletar despesa inexistente {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro ao deletar despesa {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpenseResponse>> searchExpenses(@RequestParam String search) {
        try {
            log.info("Pesquisando despesas com termo: {}", search);
            List<ExpenseResponse> expenses = expenseService.searchExpenses(search);
            log.info("Encontradas {} despesas na pesquisa", expenses.size());
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Erro ao pesquisar despesas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalExpense() {
        try {
            log.info("Calculando total de despesas");
            BigDecimal total = expenseService.getTotalExpense();
            log.info("Total de despesas: {}", total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            log.error("Erro ao calcular total de despesas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total/date-range")
    public ResponseEntity<BigDecimal> getTotalExpenseByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Calculando total de despesas entre {} e {}", startDate, endDate);
            BigDecimal total = expenseService.getTotalExpenseByDateRange(startDate, endDate);
            log.info("Total no período: {}", total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            log.error("Erro ao calcular total por período: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}