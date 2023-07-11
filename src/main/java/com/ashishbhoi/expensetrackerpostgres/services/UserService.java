package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtAuthException;
import com.ashishbhoi.expensetrackerpostgres.models.UserModel;

public interface UserService {
    UserModel validateUser(String email, String username, String password) throws EtAuthException;

    UserModel registerUser(String firstName, String lastName, String email, String username, String password)
            throws EtAuthException;
}
