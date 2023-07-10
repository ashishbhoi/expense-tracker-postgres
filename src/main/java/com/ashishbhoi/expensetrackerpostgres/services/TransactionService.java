package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.models.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

    Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId);

    Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate);

    void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction);

    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId);
}
