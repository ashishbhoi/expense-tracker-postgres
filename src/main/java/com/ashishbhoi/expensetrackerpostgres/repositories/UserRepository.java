package com.ashishbhoi.expensetrackerpostgres.repositories;

import com.ashishbhoi.expensetrackerpostgres.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByEmailOrUsername(String email, String username);

    List<User> findByEmail(String email);

    List<User> findByUsername(String username);
}
