package com.techinwork.ggkush.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TweetException extends RuntimeException {

    private HttpStatus status;

    public TweetException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
