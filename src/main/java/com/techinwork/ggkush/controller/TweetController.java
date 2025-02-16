package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.entity.Comment;
import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.service.CommentService;
import com.techinwork.ggkush.service.TweetService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173.com")
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

    @PostMapping("/{tweetId}/comments")
    public Comment addCommentByTweetId(@PathVariable("tweetId")Long tweetId, @RequestBody String text) {
        Tweet tweet = this.tweetService.findById(tweetId);
        List<Comment> comments = tweet.getComments();

        Comment comment = new Comment();
        comment.setText(text);
        comment.setTweet(tweet);

        comments.add(comment);
        this.commentService.save(comment);
        this.tweetService.save(tweet);
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
