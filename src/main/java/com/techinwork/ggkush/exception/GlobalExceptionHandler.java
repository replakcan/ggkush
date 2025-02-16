package com.techinwork.ggkush.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TweetNotFoundException.class)
    public ResponseEntity<TweetExceptionResponse> handleTweetNotFound(TweetNotFoundException e) {
        TweetExceptionResponse exceptionResponse = new TweetExceptionResponse(e.getStatus().value(), e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(exceptionResponse, e.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserExceptionResponse> handleUserException(UserNotFoundException e) {
        UserExceptionResponse exceptionResponse = new UserExceptionResponse(e.getStatus().value(), e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(exceptionResponse, e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserExceptionResponse> handleGlobalException(Exception exception) {
        UserExceptionResponse exceptionResponse = new UserExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
