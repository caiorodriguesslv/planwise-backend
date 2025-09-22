package com.devilish.planwise.repository;

import com.devilish.planwise.entities.Income;
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
public interface IncomeRepository extends JpaRepository<Income, Long> {
    
    Page<Income> findByUserAndActiveTrueOrderByDateDesc(Long userId, Pageable pageable);
    
    List<Income> findByUserAndActiveTrueOrderByDateDesc(Long userId);
    
    List<Income> findByUserAndCategoryAndActiveTrueOrderByDateDesc(Long userId, Long categoryId);
    
    List<Income> findByUserAndDateBetweenAndActiveTrueOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);
    
    Optional<Income> findByIdAndUserAndActiveTrue(Long id, Long userId);
    
    @Query("SELECT SUM(i.value) FROM Income i WHERE i.user.id = :userId AND i.active = true")
    BigDecimal getTotalIncomeByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(i.value) FROM Income i WHERE i.user.id = :userId AND i.active = true " +
           "AND i.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalIncomeByUserAndDateRange(@Param("userId") Long userId, 
                                               @Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);
    
    @Query("SELECT i FROM Income i WHERE i.user.id = :userId AND i.active = true AND " +
           "(LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Income> findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(@Param("userId") Long userId, 
                                                                           @Param("search") String search);
    
    long countByUserAndActiveTrue(Long userId);
    
    long countByUserAndDateBetweenAndActiveTrue(Long userId, LocalDate startDate, LocalDate endDate);
}
