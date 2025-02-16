package com.techinwork.ggkush.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TweetNotFoundException extends RuntimeException {
    private HttpStatus status = HttpStatus.NOT_FOUND;

    public TweetNotFoundException() {
        super("Tweet Not Found");
    }
}
