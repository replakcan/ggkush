package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.dto.CommentResponse;
import com.techinwork.ggkush.dto.TweetResponse;
import com.techinwork.ggkush.dto.UserResponse;
import com.techinwork.ggkush.entity.Comment;
import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.entity.User;
import com.techinwork.ggkush.entity.UserInteraction;
import com.techinwork.ggkush.exception.TweetNotFoundException;
import com.techinwork.ggkush.exception.UserException;
import com.techinwork.ggkush.exception.UserNotFoundException;
import com.techinwork.ggkush.repository.IAuthenticationFacade;
import com.techinwork.ggkush.service.TweetService;
import com.techinwork.ggkush.service.UserInteractionService;
import com.techinwork.ggkush.service.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private TweetService tweetService;
    private UserInteractionService userInteractionService;
    private IAuthenticationFacade authenticationFacade;

    private static @NotNull List<CommentResponse> getCommentResponses(Tweet tweet) {
        List<CommentResponse> commentResponseList = new ArrayList<>();
        List<Comment> comments = tweet.getComments();
        Iterator<Comment> commentIterator = comments.iterator();
        while(commentIterator.hasNext()) {
            Comment currentComment = commentIterator.next();

            commentResponseList.add(new CommentResponse(currentComment.getId(), currentComment.getText(), currentComment.getVotes(), currentComment.getUser().getNickName(), currentComment.getCreatedAt()));
        }
        return commentResponseList;
    }

    @GetMapping("/{userId}/tweets/{tweetId}")
    public TweetResponse findTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        if (userService.findById(userId) == null) throw new UserNotFoundException();

        try {
            User user = this.userService.findById(userId);
            Tweet tweet = this.tweetService.findById(tweetId);

            return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
        } catch (RuntimeException e) {
            throw new TweetNotFoundException();
        }
    }

    @GetMapping("/{userId}/tweets")
    public List<TweetResponse> findAllTweetsByUserId(@PathVariable("userId")Long userId) {
        if (userService.findById(userId) == null) throw new UserNotFoundException();

        List<Tweet> tweets = this.userService.findAllUserTweets(userId);
        List<TweetResponse> tweetResponses = new ArrayList<>();
        Iterator<Tweet> iterator = tweets.iterator();
        while(iterator.hasNext()) {
            Tweet currentTweet = iterator.next();
            TweetResponse currentResponse = new TweetResponse(currentTweet.getId(), currentTweet.getText(), currentTweet.getVotes(), currentTweet.getUser().getNickName(), currentTweet.getCreatedAt(), getCommentResponses(currentTweet));

            tweetResponses.add(currentResponse);
        }
        return tweetResponses;
    }

    @GetMapping("/{userId}")
    public UserResponse findUserById(@PathVariable("userId")Long userId) {
        try {
            User user = this.userService.findById(userId);
            return new UserResponse(user.getId(), user.getNickName(), user.getFirstName() + " " + user.getLastName(), user.getAge());
        } catch (RuntimeException e) {
            throw new UserNotFoundException();
        }
    }

    @GetMapping
    public List<UserResponse> findAllUsers() {
        List<User> users = this.userService.findAll();
        List<UserResponse> responses = new ArrayList<>();
        Iterator<User> userIterator = users.iterator();
        while(userIterator.hasNext()) {
            User currentUser = userIterator.next();
            responses.add(new UserResponse(currentUser.getId(), currentUser.getNickName(), currentUser.getFirstName() + " " + currentUser.getLastName() ,currentUser.getAge()));
        }
        return responses;
    }

    @PostMapping("/{userId}/tweets")
    public TweetResponse addTweetByUserId(@PathVariable("userId")Long userId, @RequestBody Tweet tweet) {
        User user = userService.findById(userId);
        if (user == null) throw new UserNotFoundException();

        Authentication authentication = this.authenticationFacade.getAuthentication();

        String authEmail = authentication.getName();
        User authUser = this.userService.findByEmail(authEmail);
        if (authUser != user) {
            throw new UserException("chill bro this is not your account", HttpStatus.FORBIDDEN);
        }

        List<CommentResponse> commentList = new ArrayList<>();
        tweet.setUser(user);
        this.tweetService.save(tweet);
        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), commentList);
    }

    @PatchMapping("/{userId}/tweets/{tweetId}")
    public TweetResponse editTweet(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId, @RequestBody String text) {

        if (this.userService.findById(userId) == null) throw new UserNotFoundException();

        Authentication authentication = authenticationFacade.getAuthentication();

        String authEmail = authentication.getName();
        User authUser = this.userService.findByEmail(authEmail);
        Tweet tweet = this.tweetService.findById(tweetId);
        User postUser = tweet.getUser();

        if (postUser != authUser) throw new UserException("You can't edit a tweet that doesn't belong to you.", HttpStatus.FORBIDDEN);
        
        tweet.setText(text);
        this.tweetService.save(tweet);

        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), postUser.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/like")
    public TweetResponse likeTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        User user = this.userService.findById(userId);
        if (user == null) throw new UserNotFoundException();

        Tweet tweet = this.tweetService.findById(tweetId);
        if (tweet == null) throw new TweetNotFoundException();

        UserInteraction userInteraction = this.userInteractionService.findByIds(userId, tweetId);

        if (userInteraction == null) {
            userInteraction = new UserInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setTweetId(tweetId);
        }

        if (userInteraction.isLike()) {
            return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
        }

        Integer votes = tweet.getVotes();
        tweet.setVotes(userInteraction.isDislike() ? votes + 2 : votes + 1);
        userInteraction.setLike(true);
        userInteraction.setDislike(false);

        this.userInteractionService.save(userInteraction);
        this.tweetService.save(tweet);
        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/dislike")
    public TweetResponse dislikeTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        User user = this.userService.findById(userId);
        if (user == null) throw new UserNotFoundException();

        Tweet tweet = this.tweetService.findById(tweetId);
        if (tweet == null) throw new TweetNotFoundException();

        UserInteraction userInteraction = this.userInteractionService.findByIds(userId, tweetId);

        if (userInteraction == null) {
            userInteraction = new UserInteraction();
            userInteraction.setUserId(userId);
            userInteraction.setTweetId(tweetId);
        }

        if (userInteraction.isDislike()) {
            return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
        }

        int votes = tweet.getVotes();
        tweet.setVotes(userInteraction.isLike() ? votes - 2 : votes - 1);
        userInteraction.setLike(false);
        userInteraction.setDislike(true);

        this.userInteractionService.save(userInteraction);
        this.tweetService.save(tweet);
        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/retweet")
    public TweetResponse retweetTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        User user = this.userService.findById(userId);
        if (user == null) throw new UserNotFoundException();

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
        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
    }

    @PatchMapping("/{userId}/tweets/{tweetId}/undo-retweet")
    public TweetResponse undoRetweetTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        User user = this.userService.findById(userId);
        if (user == null) throw new UserNotFoundException();

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
        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
    }

    @DeleteMapping("/{userId}/tweets/{tweetId}")
    public TweetResponse deleteTweetById(@PathVariable("userId")Long userId, @PathVariable("tweetId")Long tweetId) {
        User user = this.userService.findById(userId);
        if (user == null) throw new UserNotFoundException();

        Tweet tweet = this.tweetService.findById(tweetId);
        if (tweet == null) throw new TweetNotFoundException();

        Authentication authentication = authenticationFacade.getAuthentication();

        String authEmail = authentication.getName();
        User authUser = this.userService.findByEmail(authEmail);

        if (authUser != user) throw new UserException("You can't delete a post that doesn't belong to you", HttpStatus.FORBIDDEN);

        return new TweetResponse(tweet.getId(), tweet.getText(), tweet.getVotes(), user.getNickName(), tweet.getCreatedAt(), getCommentResponses(tweet));
    }
}
