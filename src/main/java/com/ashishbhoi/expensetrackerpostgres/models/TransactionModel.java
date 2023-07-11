package com.ashishbhoi.expensetrackerpostgres.models;

public record TransactionModel(Integer id, Double amount, String note, Long transactionDate) {
}
