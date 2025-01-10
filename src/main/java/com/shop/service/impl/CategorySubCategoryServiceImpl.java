package com.shop.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entitys.Category;
import com.shop.entitys.Image;
import com.shop.entitys.Subcategory;
import com.shop.repository.CategoryRepository;
import com.shop.repository.SubCategoryRepository;
import com.shop.service.CategorySubCategoryService;
import com.shop.service.FileService;

@Service
public class CategorySubCategoryServiceImpl implements CategorySubCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private FileService fileService;

    // @Autowired
    // private ImageRepository imageRepository;

    // Create or update category
    @Override
    public Category saveCategory(Category category, MultipartFile file) throws IOException {

        if (!file.isEmpty()) {

            String fileName = fileService.uploadImage(file);

            // Create a new Photo entity
            category.setImage(fileName.toString());

        }
        return categoryRepository.save(category);
    }



    // Get all categories
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Delete category by ID
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // ============================================================ SubCategory
    // Create or update subcategory
    @Override
    public Subcategory saveSubcategory(Subcategory subcategory, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {

            String fileName = fileService.uploadImage(file);

            // Create a new Photo entity
            subcategory.setImage(fileName.toString());

        }
        return subCategoryRepository.save(subcategory);
    }

    // Get all subcategories for a specific category
    @Override
    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    // Get subcategory by ID
    @Override
    public Optional<Subcategory> getSubcategoryById(Long id) {
        return subCategoryRepository.findById(id);
    }

    // Delete subcategory by ID
    @Override
    public void deleteSubcategory(Long id) {
        subCategoryRepository.deleteById(id);
    }

}
