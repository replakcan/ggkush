package com.techinwork.ggkush.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException {

    private HttpStatus status;

    public UserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
