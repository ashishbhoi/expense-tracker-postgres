package com.ashishbhoi.expensetrackerpostgres.controllers;

import com.ashishbhoi.expensetrackerpostgres.models.Transaction;
import com.ashishbhoi.expensetrackerpostgres.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> getAllTransactions(HttpServletRequest request,
                                                                        @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        List<Map<String, Object>> transactionMapList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionMapList.add(transactionMap(transaction));
        }
        return new ResponseEntity<>(transactionMapList, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Map<String, Object>> getTransactionById(HttpServletRequest request,
                                                                  @PathVariable("categoryId") Integer categoryId,
                                                                  @PathVariable("transactionId") Integer transactionId) {
        Integer userId = (Integer) request.getAttribute("userId");
        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transactionMap(transaction), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addTransaction(HttpServletRequest request,
                                                              @PathVariable("categoryId") Integer categoryId,
                                                              @RequestBody Map<String, Object> transactionMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        Double amount = Double.parseDouble(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        Long transactionDate = Long.parseLong(transactionMap.get("transactionDate").toString());
        Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);
        return new ResponseEntity<>(transactionMap(transaction), HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
                                                                  @PathVariable("categoryId") Integer categoryId,
                                                                  @PathVariable("transactionId") Integer transactionId,
                                                                  @RequestBody Transaction transaction) {
        Integer userId = (Integer) request.getAttribute("userId");
        transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> deleteTransaction(HttpServletRequest request,
                                                                  @PathVariable("categoryId") Integer categoryId,
                                                                  @PathVariable("transactionId") Integer transactionId) {
        Integer userId = (Integer) request.getAttribute("userId");
        transactionService.removeTransaction(userId, categoryId, transactionId);
        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }

    private Map<String, Object> transactionMap(Transaction transaction) {
        return Map.of("transactionId", transaction.getId(), "amount", transaction.getAmount(),
                "note", transaction.getNote(), "transactionDate", transaction.getTransactionDate());
    }
}
