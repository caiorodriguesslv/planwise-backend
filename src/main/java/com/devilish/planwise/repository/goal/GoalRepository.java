package com.devilish.planwise.repository;

import com.devilish.planwise.entities.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    
    Page<Goal> findByUserAndActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Goal> findByUserAndActiveTrueOrderByCreatedAtDesc(Long userId);
    
    List<Goal> findByUserAndStatusAndActiveTrueOrderByCreatedAtDesc(Long userId, Goal.GoalStatus status);
    
    List<Goal> findByUserAndDeadlineBeforeAndStatusNotAndActiveTrueOrderByDeadlineAsc(Long userId, 
                                                                                      LocalDate date, 
                                                                                      Goal.GoalStatus status);
    
    Optional<Goal> findByIdAndUserAndActiveTrue(Long id, Long userId);
    
    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId AND g.active = true AND " +
           "(LOWER(g.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Goal> findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(@Param("userId") Long userId, 
                                                                         @Param("search") String search);
    
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.id = :userId AND g.status = :status AND g.active = true")
    Long countByUserAndStatusAndActiveTrue(@Param("userId") Long userId, @Param("status") Goal.GoalStatus status);
    
    long countByUserAndActiveTrue(Long userId);
}
