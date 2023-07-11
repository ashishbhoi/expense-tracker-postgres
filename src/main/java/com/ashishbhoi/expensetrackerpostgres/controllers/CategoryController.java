package com.ashishbhoi.expensetrackerpostgres.controllers;

import com.ashishbhoi.expensetrackerpostgres.models.CategoryModel;
import com.ashishbhoi.expensetrackerpostgres.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryModel>> getAllCategories(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<CategoryModel> categoryModels = categoryService.fetchAllCategories(userId);
        return new ResponseEntity<>(categoryModels, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryModel> getCategoryById(HttpServletRequest request,
                                                         @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) request.getAttribute("userId");
        CategoryModel categoryModel = categoryService.fetchCategoryById(userId, categoryId);
        return new ResponseEntity<>(categoryModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CategoryModel> addCategory(HttpServletRequest request,
                                                     @RequestBody CategoryModel categoryModel) {
        Integer userId = (Integer) request.getAttribute("userId");
        CategoryModel newCategoryModel = categoryService.addCategory(userId, categoryModel.title(), categoryModel.description());
        return new ResponseEntity<>(newCategoryModel, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId,
                                                               @RequestBody CategoryModel categoryModel) {
        Integer userId = (Integer) request.getAttribute("userId");
        categoryService.updateCategory(userId, categoryId, categoryModel);
        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) request.getAttribute("userId");
        categoryService.removeCategoryWithAllTransactions(userId, categoryId);
        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }
}
