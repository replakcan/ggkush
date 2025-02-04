package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.entity.Comment;
import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.service.CommentService;
import com.techinwork.ggkush.service.TweetService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO [Alper] Same person shouldn't be able to like a tweet more than once
// TODO [Alper] Same person shouldn't be able to dislike a tweet more than once

@AllArgsConstructor
@RestController
@RequestMapping("/tweets")
public class TweetController {

    private TweetService tweetService;
    private CommentService commentService;

    @GetMapping
    public List<Tweet> findAllTweets() {
        return this.tweetService.findAll();
    }

    @DeleteMapping("/{tweetId}/retweet/{retweetId}")
    public Tweet removeRetweetByRetweetId(@PathVariable("tweetId")Long tweetId, @PathVariable("retweetId")Long retweetId) {
        return null;
    }

    @PostMapping("/{tweetId}/retweet")
    public Tweet retweetByTweetId(@PathVariable("tweetId")Long tweetId) {
        return null;
    }

    @PostMapping("/{tweetId}/comments")
    public Comment addCommentByTweetId(@PathVariable("tweetId")Long tweetId, @RequestBody Comment comment) {
        Tweet tweet = this.tweetService.findById(tweetId);
        comment.setTweet(tweet);
        this.commentService.save(comment);
        return comment;
    }

    @PatchMapping("/{tweetId}/{comments}/{commentId}")
    public Comment patchCommentById(@PathVariable("commentId")Long commentId, @RequestBody String text) {
        Comment comment = this.commentService.findById(commentId);
        comment.setText(text);
        this.commentService.save(comment);

        return comment;
    }

    @DeleteMapping("/{tweetId}/{comments}/{commentId}")
    public Comment deleteCommentById(@PathVariable("commentId")Long commentId) {
        return this.commentService.delete(commentId);
    }
}
