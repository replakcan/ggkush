package com.techinwork.ggkush.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TweetExceptionResponse {
    private int status;
    private String message;
    private Long timestamp;
}
