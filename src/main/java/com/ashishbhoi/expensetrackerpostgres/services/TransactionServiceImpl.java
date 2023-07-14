package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.entities.Category;
import com.ashishbhoi.expensetrackerpostgres.entities.Transaction;
import com.ashishbhoi.expensetrackerpostgres.entities.User;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtBadRequestException;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtResourceNotFoundException;
import com.ashishbhoi.expensetrackerpostgres.models.TransactionModel;
import com.ashishbhoi.expensetrackerpostgres.repositories.CategoryRepository;
import com.ashishbhoi.expensetrackerpostgres.repositories.TransactionRepository;
import com.ashishbhoi.expensetrackerpostgres.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository,
                                  UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TransactionModel> fetchAllTransactions(Integer userId, Integer categoryId) {
        checkCategory(userId, categoryId);
        List<Transaction> transactions = transactionRepository.findByUser_IdAndCategory_Id(userId, categoryId);
        List<TransactionModel> transactionModels = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionModels.add(new TransactionModel(transaction.getId(), transaction.getAmount(),
                    transaction.getNote(), transaction.getTransactionDate()));
        }
        return transactionModels;
    }

    @Override
    public TransactionModel fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        checkCategory(userId, categoryId);
        Transaction transaction = transactionRepository
                .findByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
        if (transaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
        return new TransactionModel(transaction.getId(), transaction.getAmount(), transaction.getNote(),
                transaction.getTransactionDate());
    }

    @Override
    public TransactionModel addTransaction(Integer userId, Integer categoryId, Double amount, String note,
                                           Long transactionDate) throws EtBadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EtResourceNotFoundException("User not found"));
        Category category = categoryRepository.findByUser_IdAndId(userId, categoryId);
        if (category == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .amount(amount)
                .note(note)
                .transactionDate(transactionDate)
                .category(category)
                .user(user)
                .build());
        return new TransactionModel(transaction.getId(), transaction.getAmount(), transaction.getNote(),
                transaction.getTransactionDate());
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId,
                                  TransactionModel transactionModel) throws EtBadRequestException {
        checkCategory(userId, categoryId);
        Transaction oldTransaction = transactionRepository
                .findByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
        if (oldTransaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
        if (transactionModel.amount() != null) {
            oldTransaction.setAmount(transactionModel.amount());
        }
        if (transactionModel.note() != null) {
            oldTransaction.setNote(transactionModel.note());
        }
        if (transactionModel.transactionDate() != null) {
            oldTransaction.setTransactionDate(transactionModel.transactionDate());
        }
        transactionRepository.save(oldTransaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        checkCategory(userId, categoryId);
        Transaction transaction = transactionRepository
                .findByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
        if (transaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
        transactionRepository
                .deleteByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
    }

    private void checkCategory(Integer userId, Integer categoryId) {
        Category category = categoryRepository.findByUser_IdAndId(userId, categoryId);
        if (category == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }
}
