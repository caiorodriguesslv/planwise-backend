package com.devilish.planwise.repository.expense;

import com.devilish.planwise.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    Page<Expense> findByUserIdAndActiveTrueOrderByDateDesc(Long userId, Pageable pageable);

    List<Expense> findByUserIdAndActiveTrueOrderByDateDesc(Long userId);

    List<Expense> findByUserIdAndCategoryIdAndActiveTrueOrderByDateDesc(Long userId, Long categoryId);

    List<Expense> findByUserIdAndDateBetweenAndActiveTrueOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);
    
    Optional<Expense> findByIdAndUserIdAndActiveTrue(Long id, Long userId);
    
    @Query("SELECT e FROM Expense e JOIN FETCH e.category c WHERE e.id = :id AND e.user.id = :userId AND e.active = true")
    Optional<Expense> findByIdAndUserIdAndActiveTrueWithCategory(@Param("id") Long id, @Param("userId") Long userId);
    
    @Query("SELECT SUM(e.value) FROM Expense e WHERE e.user.id = :userId AND e.active = true")
    BigDecimal getTotalExpenseByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(e.value) FROM Expense e WHERE e.user.id = :userId AND e.active = true " +
           "AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpenseByUserAndDateRange(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.active = true AND " +
           "(LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Expense> findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(@Param("userId") Long userId, 
                                                                            @Param("search") String search);
    
    long countByUserIdAndActiveTrue(Long userId);
    
    long countByUserIdAndDateBetweenAndActiveTrueOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);
}
