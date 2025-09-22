package com.devilish.planwise.repository.category;

import com.devilish.planwise.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByUserIdAndActiveTrueOrderByNameAsc(Long userId);
    
    Page<Category> findByUserIdAndActiveTrueOrderByNameAsc(Long userId, Pageable pageable);
    
    List<Category> findByUserIdAndTypeAndActiveTrueOrderByNameAsc(Long userId, Category.CategoryType type);
    
    Optional<Category> findByIdAndUserIdAndActiveTrue(Long id, Long userId);
    
    boolean existsByNameAndUserIdAndActiveTrue(String name, Long userId);
    
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND c.active = true AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Category> findByUserAndNameContainingIgnoreCaseAndActiveTrue(@Param("userId") Long userId, 
                                                                      @Param("search") String search);
}
