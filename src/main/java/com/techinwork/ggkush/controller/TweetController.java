package com.techinwork.ggkush.controller;

import com.techinwork.ggkush.dto.CommentResponse;
import com.techinwork.ggkush.dto.TweetResponse;
import com.techinwork.ggkush.entity.Comment;
import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.entity.User;
import com.techinwork.ggkush.exception.TweetNotFoundException;
import com.techinwork.ggkush.exception.UserException;
import com.techinwork.ggkush.repository.IAuthenticationFacade;
import com.techinwork.ggkush.service.CommentService;
import com.techinwork.ggkush.service.TweetService;
import com.techinwork.ggkush.service.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/tweets")
public class TweetController {

    private TweetService tweetService;
    private CommentService commentService;
    private UserService userService;
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

    @GetMapping
    public List<TweetResponse> findAllTweets() {
        List<Tweet> tweetList = this.tweetService.findAll();

        List<TweetResponse> rv = new ArrayList<>();

        Iterator<Tweet> tweetIterator = tweetList.iterator();
        while(tweetIterator.hasNext()) {
            Tweet currentTweet = tweetIterator.next();
            TweetResponse tweetResponse = new TweetResponse(currentTweet.getId(), currentTweet.getText(), currentTweet.getVotes(), currentTweet.getUser().getNickName(), currentTweet.getCreatedAt(), getCommentResponses(currentTweet));
            rv.add(tweetResponse);
        }

        return rv;
    }

    @PostMapping("/{tweetId}/comments")
    public CommentResponse addCommentByTweetId(@PathVariable("tweetId")Long tweetId, @RequestBody String text) {
        Tweet tweet = this.tweetService.findById(tweetId);
        List<Comment> comments = tweet.getComments();

        Authentication authentication = this.authenticationFacade.getAuthentication();
        String authEmail = authentication.getName();

        User commentUser = this.userService.findByEmail(authEmail);

        Comment comment = new Comment();
        comment.setText(text);
        comment.setTweet(tweet);
        comment.setUser(commentUser);

        comments.add(comment);
        this.commentService.save(comment);
        this.tweetService.save(tweet);

        CommentResponse rv = new CommentResponse(comment.getId(), comment.getText(), comment.getVotes(), comment.getUser().getNickName(), comment.getCreatedAt());
        return rv;
    }

    @PatchMapping("/{tweetId}/{comments}/{commentId}")
    public CommentResponse patchCommentById(@PathVariable("tweetId")Long tweetId, @PathVariable("commentId")Long commentId, @RequestBody String text) {
        Tweet tweet = this.tweetService.findById(tweetId);
        if (tweet == null) throw new TweetNotFoundException();

        Comment comment = this.commentService.findById(commentId);
        if (comment == null) throw new RuntimeException("comment not found");

        User user = comment.getUser();

        Authentication authentication = authenticationFacade.getAuthentication();
        String authEmail = authentication.getName();
        User authUser = this.userService.findByEmail(authEmail);

        if (authUser != user) throw new UserException("you can't edit a comment that doesn't belong to you!", HttpStatus.FORBIDDEN);

        comment.setText(text);
        this.commentService.save(comment);

        CommentResponse rv = new CommentResponse(comment.getId(), comment.getText(), comment.getVotes(), comment.getUser().getNickName(), comment.getCreatedAt());

        return rv;
    }

    @DeleteMapping("/{tweetId}/{comments}/{commentId}")
    public CommentResponse deleteCommentById(@PathVariable("tweetId")Long tweetId, @PathVariable("commentId")Long commentId) {
        Tweet tweet = this.tweetService.findById(tweetId);
        Comment comment = this.commentService.findById(commentId);
        User user = comment.getUser();

        Authentication authentication = this.authenticationFacade.getAuthentication();
        String authEmail = authentication.getName();
        User authUser = this.userService.findByEmail(authEmail);

        if (authUser != user) throw new UserException("you can't delete a comment that doesn't belong to you!", HttpStatus.FORBIDDEN);

        CommentResponse rv = new CommentResponse(comment.getId(), comment.getText(), comment.getVotes(), authUser.getNickName(), comment.getCreatedAt());

        this.commentService.delete(commentId);

        return rv;
    }
}
