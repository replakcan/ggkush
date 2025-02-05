package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.entity.User;
import com.techinwork.ggkush.entity.UserInteraction;
import com.techinwork.ggkush.exception.TweetException;
import com.techinwork.ggkush.exception.TweetNotFoundException;
import com.techinwork.ggkush.exception.UserException;
import com.techinwork.ggkush.exception.UserNotFoundException;
import com.techinwork.ggkush.service.TweetService;
import com.techinwork.ggkush.service.UserInteractionService;
import com.techinwork.ggkush.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO [Alper] change return values with response records

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private TweetService tweetService;
    private UserInteractionService userInteractionService;

    @GetMapping("/{userId}/tweets/{tweetId}")
    public Tweet findTweetById(@PathVariable("tweetId")Long tweetId) {
        try {
            return this.tweetService.findById(tweetId);
        } catch (RuntimeException e) {
            throw new TweetNotFoundException();
        }
    }

    @GetMapping("/{userId}/tweets")
    public List<Tweet> findAllTweetsByUserId(@PathVariable("userId")Long userId) {
        return this.userService.findAllUserTweets(userId);
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable("userId")Long userId) {
        try {
            return this.userService.findById(userId);
        } catch (RuntimeException e) {
            throw new UserNotFoundException();
        }

    }

    @GetMapping
    public List<User> findAllUsers() {
        return this.userService.findAll();
    }

    @PostMapping("/{userId}/tweets")
    public Tweet addTweetByUserId(@PathVariable("userId")Long userId, @RequestBody Tweet tweet) {
        User user = this.userService.findById(userId);

        if (user == null) throw new UserNotFoundException();

        tweet.setUser(user);
        this.tweetService.save(tweet);
        return tweet;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return this.userService.save(user);
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/like")
    public Tweet likeTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        Tweet tweet = this.tweetService.findById(tweetId);

        if (tweet == null) throw new TweetNotFoundException();

        UserInteraction userInteraction = this.userInteractionService.findByIds(userId, tweetId);

        if (userInteraction == null) {
            userInteraction = new UserInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setTweetId(tweetId);
        }
        userInteraction.setLike(true);
        userInteraction.setDislike(false);

        Integer votes = tweet.getVotes();
        tweet.setVotes(votes + 1);
        this.userInteractionService.save(userInteraction);
        this.tweetService.save(tweet);
        return tweet;
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/dislike")
    public Tweet dislikeTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        Tweet tweet = this.tweetService.findById(tweetId);

        if (tweet == null) throw new TweetNotFoundException();

        UserInteraction userInteraction = this.userInteractionService.findByIds(userId, tweetId);

        if (userInteraction == null) {
            userInteraction = new UserInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setTweetId(tweetId);
        }
        userInteraction.setLike(false);
        userInteraction.setDislike(true);

        Integer votes = tweet.getVotes();
        tweet.setVotes(votes - 1);
        this.userInteractionService.save(userInteraction);
        this.tweetService.save(tweet);
        return tweet;
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/retweet")
    public Tweet retweetTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        Tweet tweet = this.tweetService.findById(tweetId);

        if (tweet == null) throw new TweetNotFoundException();

        UserInteraction userInteraction = this.userInteractionService.findByIds(userId, tweetId);

        if (userInteraction == null) {
            userInteraction = new UserInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setTweetId(tweetId);
        }
        userInteraction.setRetweet(true);

        this.userInteractionService.save(userInteraction);
        return tweet;
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/undo-retweet")
    public Tweet undoRetweetTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        Tweet tweet = this.tweetService.findById(tweetId);

        if (tweet == null) throw new TweetNotFoundException();

        UserInteraction userInteraction = this.userInteractionService.findByIds(userId, tweetId);

        if (userInteraction == null) {
            userInteraction = new UserInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setTweetId(tweetId);
        }
        userInteraction.setRetweet(false);

        this.userInteractionService.save(userInteraction);
        return tweet;
    }

    @PatchMapping("/{userId}/tweets/{tweetId}")
    public Tweet patchTweetById(@PathVariable("tweetId")Long tweetId, @RequestBody String text) {
        Tweet tweet = this.tweetService.findById(tweetId);

        if (tweet == null) throw new TweetNotFoundException();

        tweet.setText(text);
        this.tweetService.save(tweet);

        return tweet;
    }

    @DeleteMapping("/{userId}/tweets/{tweetId}")
    public Tweet deleteTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        User user = this.userService.findById(userId);
        Tweet tweet = this.tweetService.findById(tweetId);

        if (tweet.getUser() != user) throw new UserException("Only who post the tweet can delete it.", HttpStatus.UNAUTHORIZED);

        return this.tweetService.delete(tweetId);
    }
}
