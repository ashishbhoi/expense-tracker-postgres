package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtBadRequestException;
import com.ashishbhoi.expensetrackerpostgres.exceptions.EtResourceNotFoundException;
import com.ashishbhoi.expensetrackerpostgres.models.TransactionModel;

import java.util.List;

public interface TransactionService {
    List<TransactionModel> fetchAllTransactions(Integer userId, Integer categoryId);

    TransactionModel fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException;

    TransactionModel addTransaction(Integer userId, Integer categoryId, Double amount, String note,
                                    Long transactionDate) throws EtBadRequestException;

    void updateTransaction(Integer userId, Integer categoryId, Integer transactionId,
                           TransactionModel transactionModel) throws EtBadRequestException;

    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException;
}
