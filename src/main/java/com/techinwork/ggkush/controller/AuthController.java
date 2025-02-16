package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.dto.LoginRequest;
import com.techinwork.ggkush.dto.LoginResponse;
import com.techinwork.ggkush.dto.RegisterResponse;
import com.techinwork.ggkush.entity.User;
import com.techinwork.ggkush.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
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

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.loginControl(loginRequest);
    }
}
