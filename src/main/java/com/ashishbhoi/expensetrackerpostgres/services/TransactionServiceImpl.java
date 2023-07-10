package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtResourceNotFoundException;
import com.ashishbhoi.expensetrackerpostgres.models.Category;
import com.ashishbhoi.expensetrackerpostgres.models.Transaction;
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
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        Category category = categoryRepository.findByUser_IdAndId(userId, categoryId);
        if (category == null) {
            throw new EtResourceNotFoundException("Category not found");
        }
        return transactionRepository.findByUser_IdAndCategory_Id(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) {
        Transaction transaction = transactionRepository
                .findByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
        if (transaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
        return transaction;
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note,
                                      Long transactionDate) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EtResourceNotFoundException("User not found"));
        Category category = categoryRepository.findByUser_IdAndId(userId, categoryId);
        return transactionRepository.save(Transaction.builder()
                .amount(amount)
                .note(note)
                .transactionDate(transactionDate)
                .category(category)
                .user(user)
                .build());
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) {
        Transaction oldTransaction = transactionRepository
                .findByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
        if (oldTransaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
        if (transaction.getAmount() != null) {
            oldTransaction.setAmount(transaction.getAmount());
        }
        if (transaction.getNote() != null) {
            oldTransaction.setNote(transaction.getNote());
        }
        if (transaction.getTransactionDate() != null) {
            oldTransaction.setTransactionDate(transaction.getTransactionDate());
        }
        transactionRepository.save(oldTransaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) {
        Transaction transaction = transactionRepository
                .findByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
        if (transaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
        transactionRepository
                .deleteByUser_IdAndCategory_IdAndId(userId, categoryId, transactionId);
    }
}
