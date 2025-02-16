package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.UserInteraction;
import com.techinwork.ggkush.repository.UserInteractionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserInteractionServiceImpl implements UserInteractionService{

    private UserInteractionRepository userInteractionRepository;

    @Override
    public UserInteraction save(UserInteraction userInteraction) {
        return this.userInteractionRepository.save(userInteraction);
    }

    @Override
    public UserInteraction delete(UserInteraction userInteraction) {
        this.userInteractionRepository.delete(userInteraction);
        return userInteraction;
    }

    @Override
    public UserInteraction findByIds(Long userId, Long tweetId) {
        return this.userInteractionRepository.findByIds(userId, tweetId);
    }
}
