package com.shop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.entitys.Category;
import com.shop.entitys.Subcategory;
import com.shop.service.CategorySubCategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategorySubCategoryService categorySubCategoryService;

    // Create or update category
    @PostMapping
    public ResponseEntity<Category> createOrUpdateCategory(
            @RequestParam("category") String categoryJson,
            @RequestParam("file") MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Category category = objectMapper.readValue(categoryJson, Category.class);

        Category savedCategory = categorySubCategoryService.saveCategory(category, file);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categorySubCategoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> category = categorySubCategoryService.getCategoryById(id);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categorySubCategoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =======================================================================SubCategory===============
    // Create or update subcategory
    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<Subcategory> createOrUpdateSubcategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam("subcategory") String subcategoryJson,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Parse the JSON string into a Subcategory object
        ObjectMapper objectMapper = new ObjectMapper();
        Subcategory subcategory = objectMapper.readValue(subcategoryJson, Subcategory.class);

        // Retrieve Category by ID
        Optional<Category> category = categorySubCategoryService.getCategoryById(categoryId);
        if (category.isPresent()) {
            // Set the category and image data in Subcategory
            subcategory.setCategory(category.get());

            // Save subcategory and return response
            Subcategory savedSubcategory = categorySubCategoryService.saveSubcategory(subcategory, file);
            return new ResponseEntity<>(savedSubcategory, HttpStatus.CREATED);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all subcategories for a category
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<Subcategory>> getSubcategoriesByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<Subcategory> subcategories = categorySubCategoryService.getSubcategoriesByCategoryId(categoryId);
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    // Get subcategory by ID
    @GetMapping("/subcategories/{id}")
    public ResponseEntity<Subcategory> getSubcategoryById(@PathVariable("id") Long id) {
        Optional<Subcategory> subcategory = categorySubCategoryService.getSubcategoryById(id);
        return subcategory.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete subcategory by ID
    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<Void> deleteSubcategory(@PathVariable("id") Long id) {
        categorySubCategoryService.deleteSubcategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
