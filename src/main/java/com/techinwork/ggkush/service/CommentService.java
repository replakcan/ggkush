package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.Comment;

public interface CommentService {
    Comment findById(Long commentId);
    Comment delete(Long commentId);
    Comment save(Comment comment);
}
