package com.techinwork.ggkush.repository;

import com.techinwork.ggkush.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    @Query(value = "SELECT * FROM ggkush.user_interaction AS u_i WHERE u_i.user_id = :userId AND u_i.tweet_id = :tweetId", nativeQuery = true)
    UserInteraction findByIds(Long userId, Long tweetId);
}
