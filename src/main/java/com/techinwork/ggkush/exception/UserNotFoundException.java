package com.techinwork.ggkush.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException {
    private HttpStatus status = HttpStatus.NOT_FOUND;

    public UserNotFoundException() {
        super("User Not Found");
    }
}
