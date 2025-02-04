package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> findAll();
    Tweet findById(Long tweetId);
    Tweet delete(Long tweetId);
    Tweet save(Tweet tweet);
}
