package com.devilish.planwise.services.category;

import com.devilish.planwise.dto.category.CategoryRequest;
import com.devilish.planwise.dto.category.CategoryResponse;
import com.devilish.planwise.entities.Category;
import com.devilish.planwise.entities.User;
import com.devilish.planwise.repository.category.CategoryRepository;
import com.devilish.planwise.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        
        // Verificar se já existe categoria com o mesmo nome para o usuário
        if (categoryRepository.existsByNameAndUserAndActiveTrue(request.getName(), currentUser.getId())) {
            throw new RuntimeException("Já existe uma categoria com este nome");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setUser(currentUser);
        category.setCreatedAt(LocalDateTime.now());
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromCategory(savedCategory);
    }

    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        User currentUser = userService.getCurrentUserEntity();
        Page<Category> categories = categoryRepository.findByUserAndActiveTrueOrderByNameAsc(currentUser.getId(), pageable);
        return categories.map(CategoryResponse::fromCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        User currentUser = userService.getCurrentUserEntity();
        List<Category> categories = categoryRepository.findByUserAndActiveTrueOrderByNameAsc(currentUser.getId());
        return categories.stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoriesByType(Category.CategoryType type) {
        User currentUser = userService.getCurrentUserEntity();
        List<Category> categories = categoryRepository.findByUserAndTypeAndActiveTrueOrderByNameAsc(currentUser.getId(), type);
        return categories.stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Category category = categoryRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return CategoryResponse.fromCategory(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Category category = categoryRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verificar se já existe outra categoria com o mesmo nome
        if (!category.getName().equals(request.getName()) && 
            categoryRepository.existsByNameAndUserAndActiveTrue(request.getName(), currentUser.getId())) {
            throw new RuntimeException("Já existe uma categoria com este nome");
        }

        category.setName(request.getName());
        category.setType(request.getType());

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromCategory(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        User currentUser = userService.getCurrentUserEntity();
        Category category = categoryRepository.findByIdAndUserAndActiveTrue(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Soft delete
        category.setActive(false);
        categoryRepository.save(category);
    }

    public List<CategoryResponse> searchCategories(String search) {
        User currentUser = userService.getCurrentUserEntity();
        List<Category> categories = categoryRepository.findByUserAndNameContainingIgnoreCaseAndActiveTrue(currentUser.getId(), search);
        return categories.stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }
}
