package com.techinwork.ggkush.dto;

import java.util.Date;

public record TweetResponse(Long id, String text, int votes, String nickName, Date createdDate) {
}
