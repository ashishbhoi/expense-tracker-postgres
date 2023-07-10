package com.ashishbhoi.expensetrackerpostgres.controllers;

import com.ashishbhoi.expensetrackerpostgres.models.Category;
import com.ashishbhoi.expensetrackerpostgres.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> getAllCategories(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<Category> categories = categoryService.fetchAllCategories(userId);
        List<Map<String, Object>> categoryMapList = new ArrayList<>();
        for (Category category : categories) {
            categoryMapList.add(categoryMap(category));
        }
        return new ResponseEntity<>(categoryMapList, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Map<String, Object>> getCategoryById(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(userId, categoryId);
        return new ResponseEntity<>(categoryMap(category), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addCategory(HttpServletRequest request,
                                                           @RequestBody Map<String, Object> categoryMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        String title = (String) categoryMap.get("title");
        String description = (String) categoryMap.get("description");
        Category category = categoryService.addCategory(userId, title, description);
        return new ResponseEntity<>(categoryMap(category), HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId,
                                                               @RequestBody Category category) {
        Integer userId = (Integer) request.getAttribute("userId");
        categoryService.updateCategory(userId, categoryId, category);
        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) request.getAttribute("userId");
        categoryService.removeCategoryWithAllTransactions(userId, categoryId);
        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }

    private Map<String, Object> categoryMap(Category category) {
        return Map.of("categoryId", category.getId(), "title", category.getTitle(),
                "description", category.getDescription());
    }
}
