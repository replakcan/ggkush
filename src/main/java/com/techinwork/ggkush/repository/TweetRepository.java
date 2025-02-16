package com.techinwork.ggkush.repository;

import com.techinwork.ggkush.entity.Comment;
import com.techinwork.ggkush.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query(value = "SELECT * FROM ggkush.Tweet", nativeQuery = true)
    List<Tweet> findAll();

    @Query(value = "SELECT * FROM ggkush.Comment c WHERE c.tweet_id = :tweetId", nativeQuery = true)
    List<Comment> findAllTweetComments(Long tweetId);
}
