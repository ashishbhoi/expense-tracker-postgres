package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtAuthException;
import com.ashishbhoi.expensetrackerpostgres.models.User;

public interface UserService {
    User validateUser(String email, String username, String password) throws EtAuthException;

    User registerUser(String firstName, String lastName, String email, String username, String password)
            throws EtAuthException;
}
