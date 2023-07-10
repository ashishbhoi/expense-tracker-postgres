package com.ashishbhoi.expensetrackerpostgres.repositories;

import com.ashishbhoi.expensetrackerpostgres.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUser_IdAndCategory_Id(Integer userId, Integer categoryId);

    Transaction findByUser_IdAndCategory_IdAndId(Integer userId, Integer categoryId, Integer transactionId);

    @Modifying
    void deleteByUser_IdAndCategory_IdAndId(Integer userId, Integer categoryId, Integer transactionId);

    @Modifying
    void deleteAllByUser_IdAndCategory_Id(Integer userId, Integer categoryId);
}
