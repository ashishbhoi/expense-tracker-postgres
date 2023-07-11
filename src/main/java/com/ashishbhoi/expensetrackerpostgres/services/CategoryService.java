package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtBadRequestException;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtResourceNotFoundException;
import com.ashishbhoi.expensetrackerpostgres.models.CategoryModel;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> fetchAllCategories(Integer userId);

    CategoryModel fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;

    CategoryModel addCategory(Integer userId, String title, String description) throws EtBadRequestException;

    void updateCategory(Integer userId, Integer categoryId, CategoryModel categoryModel) throws EtBadRequestException;

    void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
}
