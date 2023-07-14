package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.entities.Category;
import com.ashishbhoi.expensetrackerpostgres.entities.Transaction;
import com.ashishbhoi.expensetrackerpostgres.entities.User;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtBadRequestException;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtResourceNotFoundException;
import com.ashishbhoi.expensetrackerpostgres.models.CategoryModel;
import com.ashishbhoi.expensetrackerpostgres.repositories.CategoryRepository;
import com.ashishbhoi.expensetrackerpostgres.repositories.TransactionRepository;
import com.ashishbhoi.expensetrackerpostgres.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository,
                               TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<CategoryModel> fetchAllCategories(Integer userId) {
        List<Category> categories = categoryRepository.findByUser_Id(userId);
        List<CategoryModel> categoryModels = new ArrayList<>();
        for (Category category : categories) {
            double totalExpenses = 0.0;
            List<Transaction> transactions = transactionRepository.findByUser_IdAndCategory_Id(userId, category.getId());
            for (Transaction transaction : transactions) {
                totalExpenses += transaction.getAmount();
            }
            categoryModels.add(new CategoryModel(category.getId(), category.getTitle(), category.getDescription(),
                    totalExpenses));
        }
        return categoryModels;
    }

    @Override
    public CategoryModel fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        Category category = categoryRepository.findByUser_IdAndId(categoryId, userId);
        if (category == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        double totalExpenses = 0.0;
        List<Transaction> transactions = transactionRepository.findByUser_IdAndCategory_Id(userId, category.getId());
        for (Transaction transaction : transactions) {
            totalExpenses += transaction.getAmount();
        }
        return new CategoryModel(category.getId(), category.getTitle(), category.getDescription(), totalExpenses);
    }

    @Override
    public CategoryModel addCategory(Integer userId, String title, String description) throws EtBadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EtResourceNotFoundException("User not found"));
        Category category = categoryRepository.save(Category.builder()
                .title(title)
                .description(description)
                .user(user)
                .build());
        return new CategoryModel(category.getId(), category.getTitle(), category.getDescription(), 0.0);
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, CategoryModel categoryModel) throws EtBadRequestException {
        Category oldCategory = categoryRepository.findByUser_IdAndId(userId, categoryId);
        if (oldCategory == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        if (categoryModel.title() != null) {
            oldCategory.setTitle(categoryModel.title());
        }
        if (categoryModel.description() != null) {
            oldCategory.setDescription(categoryModel.description());
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
        categoryRepository.deleteByUser_IdAndId(userId, categoryId);
    }
}
