package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    User findById(Long userId);
    User save(User user);
    User delete(Long userId);
    List<Tweet> findAllUserTweets(Long userId);
}
