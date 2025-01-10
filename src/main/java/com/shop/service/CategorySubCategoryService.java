package com.shop.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Category;
import com.shop.entitys.Subcategory;

@Service
public interface CategorySubCategoryService {
    Category saveCategory(Category category, MultipartFile file) throws IOException;

    // Get all categories
    List<Category> getAllCategories();

    // Get category by ID
    Optional<Category> getCategoryById(Long id);

    // Delete category by ID
    void deleteCategory(Long id);

    // Create or update subcategory
    Subcategory saveSubcategory(Subcategory subcategory, MultipartFile file) throws IOException;

    // Get all subcategories for a specific category
    List<Subcategory> getSubcategoriesByCategoryId(Long categoryId);

    // Get subcategory by ID
    Optional<Subcategory> getSubcategoryById(Long id);

    // Delete subcategory by ID
    void deleteSubcategory(Long id);
}
