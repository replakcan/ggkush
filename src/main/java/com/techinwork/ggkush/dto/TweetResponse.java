package com.techinwork.ggkush.dto;

import java.util.Date;
import java.util.List;

public record TweetResponse(Long id, String text, int votes, String nickName, Date createdDate, List<CommentResponse> comments) {
}
