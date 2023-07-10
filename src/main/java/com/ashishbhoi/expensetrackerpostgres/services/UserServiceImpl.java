package com.ashishbhoi.expensetrackerpostgres.services;

import com.ashishbhoi.expensetrackerpostgres.exceptions.EtAuthException;
import com.ashishbhoi.expensetrackerpostgres.models.User;
import com.ashishbhoi.expensetrackerpostgres.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String username, String password) throws EtAuthException {
        if (email != null)
            email = email.toLowerCase();
        List<User> users = userRepository.findByEmailOrUsername(email, username);
        if (users.size() == 0)
            throw new EtAuthException("Invalid email/username or password");
        User user = users.get(0);
        if (!BCrypt.checkpw(password, user.getPassword()))
            throw new EtAuthException("Invalid email/username or password");
        return user;
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String username, String password)
            throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null && username != null) {
            email = email.toLowerCase();
            username = username.toLowerCase();
            if (!pattern.matcher(email).matches())
                throw new EtAuthException("Invalid email format");
        }
        List<User> usersByEmail = userRepository.findByEmail(email);
        if (usersByEmail.size() > 0)
            throw new EtAuthException("Email already in use");
        List<User> usersByUsername = userRepository.findByUsername(username);
        if (usersByUsername.size() > 0)
            throw new EtAuthException("Username already in use");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        return userRepository.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(username)
                .password(hashedPassword)
                .build());
    }
}
