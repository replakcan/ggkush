package com.techinwork.ggkush.dto;

import java.util.Date;

public record CommentResponse(Long id, String text, int votes, String nickName, Date createdDate) {
}
