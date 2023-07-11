package com.ashishbhoi.expensetrackerpostgres.repositories;

import com.ashishbhoi.expensetrackerpostgres.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByUser_Id(Integer userId);

    Category findByUser_IdAndId(Integer userId, Integer categoryId);

    @Modifying
    void deleteByUser_IdAndId(Integer userId, Integer categoryId);
}
