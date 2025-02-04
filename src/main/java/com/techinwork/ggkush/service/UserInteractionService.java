package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.UserInteraction;

public interface UserInteractionService{
    UserInteraction save(UserInteraction userInteraction);
    UserInteraction delete(UserInteraction userInteraction);
    UserInteraction findByIds(Long userId, Long tweetId);
}
