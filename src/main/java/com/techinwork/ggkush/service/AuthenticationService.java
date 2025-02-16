package com.techinwork.ggkush.service;

import com.techinwork.ggkush.dto.LoginRequest;
import com.techinwork.ggkush.dto.LoginResponse;
import com.techinwork.ggkush.entity.Role;
import com.techinwork.ggkush.entity.User;
import com.techinwork.ggkush.repository.RoleRepository;
import com.techinwork.ggkush.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse loginControl(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.email());

        if (user.isPresent()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(loginRequest.password(), user.get().getPassword())) {
                throw new RuntimeException("Invalid password");
            } else {
                return new LoginResponse(loginRequest.email(), loginRequest.password());
            }
        } else {
            throw new RuntimeException("Invalid email address");
        }
    }

    public User register(String email, String password, String firstName, String lastName, String nickName, Integer age) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            throw new RuntimeException("User with given email already exist! Email: " + email);
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setNickName(nickName);
        user.setAge(age);
        user.setEmail(email);
        user.setRoles(roles);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
}
