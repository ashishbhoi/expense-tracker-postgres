package com.ashishbhoi.expensetrackerpostgres.controllers;

import com.ashishbhoi.expensetrackerpostgres.models.TransactionModel;
import com.ashishbhoi.expensetrackerpostgres.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public ResponseEntity<List<TransactionModel>> getAllTransactions(HttpServletRequest request,
                                                                     @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<TransactionModel> transactionModels = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactionModels, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionModel> getTransactionById(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId,
                                                               @PathVariable("transactionId") Integer transactionId) {
        Integer userId = (Integer) request.getAttribute("userId");
        TransactionModel transactionModel = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transactionModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<TransactionModel> addTransaction(HttpServletRequest request,
                                                           @PathVariable("categoryId") Integer categoryId,
                                                           @RequestBody Map<String, Object> transactionMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        Double amount = Double.parseDouble(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        Long transactionDate = Long.parseLong(transactionMap.get("transactionDate").toString());
        TransactionModel transactionModel = transactionService.addTransaction(userId, categoryId, amount, note,
                transactionDate);
        return new ResponseEntity<>(transactionModel, HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
                                                                  @PathVariable("categoryId") Integer categoryId,
                                                                  @PathVariable("transactionId") Integer transactionId,
                                                                  @RequestBody TransactionModel transactionModel) {
        Integer userId = (Integer) request.getAttribute("userId");
        transactionService.updateTransaction(userId, categoryId, transactionId, transactionModel);
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
}
