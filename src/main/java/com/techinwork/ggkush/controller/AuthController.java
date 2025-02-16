package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.dto.RegisterResponse;
import com.techinwork.ggkush.entity.User;
import com.techinwork.ggkush.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody User user) {
        User createdUser = authenticationService.register(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getNickName(), user.getAge());
        return new RegisterResponse(createdUser.getEmail(), "Registration successful!");
    }
}
