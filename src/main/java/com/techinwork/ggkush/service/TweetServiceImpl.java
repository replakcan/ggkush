package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TweetServiceImpl implements TweetService{

    private TweetRepository tweetRepository;

    @Override
    public List<Tweet> findAll() {
        return this.tweetRepository.findAll();
    }

    @Override
    public Tweet findById(Long tweetId) {
        Optional<Tweet> tweetOptional = this.tweetRepository.findById(tweetId);

        if (tweetOptional.isPresent()) return tweetOptional.get();
        throw new RuntimeException("Tweet not found");
    }

    @Override
    public Tweet delete(Long tweetId) {
        Tweet tweet = this.findById(tweetId);
        this.tweetRepository.delete(tweet);

        return tweet;
    }

    @Override
    public Tweet save(Tweet tweet) {
        this.tweetRepository.save(tweet);
        return tweet;
    }
}

