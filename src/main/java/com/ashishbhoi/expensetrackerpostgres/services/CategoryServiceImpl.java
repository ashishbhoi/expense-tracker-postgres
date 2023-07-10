package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtBadRequestException;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtResourceNotFoundException;
import com.ashishbhoi.expensetrackerpostgres.models.Category;
import com.ashishbhoi.expensetrackerpostgres.models.User;
import com.ashishbhoi.expensetrackerpostgres.repositories.CategoryRepository;
import com.ashishbhoi.expensetrackerpostgres.repositories.TransactionRepository;
import com.ashishbhoi.expensetrackerpostgres.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Category> fetchAllCategories(Integer userId) {
        return categoryRepository.findByUser_Id(userId);
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        Category category = categoryRepository.findByUser_IdAndId(categoryId, userId);
        if (category == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        return category;
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws EtBadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EtResourceNotFoundException("User not found"));
        return categoryRepository.save(Category.builder()
                .title(title)
                .description(description)
                .user(user)
                .build());
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        Category oldCategory = categoryRepository.findByUser_IdAndId(categoryId, userId);
        if (oldCategory == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        if (category.getTitle() != null) {
            oldCategory.setTitle(category.getTitle());
        }
        if (category.getDescription() != null) {
            oldCategory.setDescription(category.getDescription());
        }
        categoryRepository.save(oldCategory);
    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId)
            throws EtResourceNotFoundException {
        Category category = categoryRepository.findByUser_IdAndId(userId, categoryId);
        if (category == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        transactionRepository.deleteAllByUser_IdAndCategory_Id(userId, categoryId);
        categoryRepository.deleteByUser_IdAndId(userId, categoryId);
    }
}
