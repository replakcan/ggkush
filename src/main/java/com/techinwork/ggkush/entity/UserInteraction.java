package com.techinwork.ggkush.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_interaction", schema = "ggkush")
public class UserInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "liked_")
    private boolean like;

    @Column(name = "disliked_")
    private boolean dislike;

    @Column(name = "retweet_")
    private boolean retweet;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tweet_id")
    private Long tweetId;

    public UserInteraction(boolean like, boolean dislike) {
        this.like = like;
        this.dislike = dislike;
    }

    public UserInteraction(boolean retweet) {
        this.retweet = retweet;
    }
}
