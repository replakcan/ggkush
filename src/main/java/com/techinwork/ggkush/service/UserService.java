package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long userId);
    User save(User user);
    User delete(Long userId);
    List<Tweet> findAllUserTweets(Long userId);
}
