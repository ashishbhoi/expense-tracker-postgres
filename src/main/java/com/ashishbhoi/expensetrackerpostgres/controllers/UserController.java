package com.ashishbhoi.expensetrackerpostgres.controllers;

import com.ashishbhoi.expensetrackerpostgres.Constants;
import com.ashishbhoi.expensetrackerpostgres.models.UserModel;
import com.ashishbhoi.expensetrackerpostgres.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        UserModel userModel = userService.validateUser(email, username, password);
        return new ResponseEntity<>(generateJWTToken(userModel), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        UserModel userModel = userService.registerUser(firstName, lastName, email, username, password);
        return new ResponseEntity<>(generateJWTToken(userModel), HttpStatus.CREATED);
    }

    private Map<String, String> generateJWTToken(UserModel userModel) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", userModel.id())
                .claim("email", userModel.email())
                .claim("username", userModel.username())
                .claim("firstName", userModel.firstName())
                .claim("lastName", userModel.lastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
