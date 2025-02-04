package com.techinwork.ggkush.repository;

import com.techinwork.ggkush.entity.Tweet;
import com.techinwork.ggkush.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u")
    List<User> findAll();

    @Query(value = "SELECT * FROM ggkush.Tweet t WHERE t.user_id = :userId", nativeQuery = true)
    List<Tweet> findAllUserTweets(Long userId);


}
