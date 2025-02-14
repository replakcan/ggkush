package com.techinwork.ggkush.controller.security;

import com.techinwork.ggkush.dto.security.RegisterResponse;
import com.techinwork.ggkush.dto.security.RegistrationMember;
import com.techinwork.ggkush.entity.security.Member;
import com.techinwork.ggkush.service.security.AuthenticationService;
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
    public RegisterResponse register(@RequestBody RegistrationMember registrationMember) {
        Member createdMember = authenticationService.register(registrationMember.email(), registrationMember.password());
        return new RegisterResponse(createdMember.getEmail(), "kayıt başarılı bir şekilde gerçekleşti.");
    }
}
