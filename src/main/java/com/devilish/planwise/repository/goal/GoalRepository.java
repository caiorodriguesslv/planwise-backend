package com.devilish.planwise.repository.goal;

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
    
    Page<Goal> findByUserIdAndActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Goal> findByUserIdAndActiveTrueOrderByCreatedAtDesc(Long userId);
    
    List<Goal> findByUserIdAndStatusAndActiveTrueOrderByCreatedAtDesc(Long userId, Goal.GoalStatus status);
    
    List<Goal> findByUserIdAndDeadlineBeforeAndStatusNotAndActiveTrueOrderByDeadlineAsc(Long userId, 
                                                                                         LocalDate date, 
                                                                                         Goal.GoalStatus status);
    
    Optional<Goal> findByIdAndUserIdAndActiveTrue(Long id, Long userId);
    
    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId AND g.active = true AND " +
           "(LOWER(g.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Goal> findByUserAndDescriptionContainingIgnoreCaseAndActiveTrue(@Param("userId") Long userId, 
                                                                         @Param("search") String search);
    
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.id = :userId AND g.status = :status AND g.active = true")
    Long countByUserAndStatusAndActiveTrue(@Param("userId") Long userId, @Param("status") Goal.GoalStatus status);
    
    long countByUserIdAndActiveTrue(Long userId);
}
