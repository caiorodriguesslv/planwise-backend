package com.devilish.planwise.repository;

import com.devilish.planwise.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByUserAndActiveTrueOrderByNameAsc(Long userId);
    
    List<Category> findByUserAndTypeAndActiveTrueOrderByNameAsc(Long userId, Category.CategoryType type);
    
    Optional<Category> findByIdAndUserAndActiveTrue(Long id, Long userId);
    
    boolean existsByNameAndUserAndActiveTrue(String name, Long userId);
    
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND c.active = true AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Category> findByUserAndNameContainingIgnoreCaseAndActiveTrue(@Param("userId") Long userId, 
                                                                      @Param("search") String search);
}
