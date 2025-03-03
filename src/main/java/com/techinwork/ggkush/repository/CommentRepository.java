package com.techinwork.ggkush.repository;

import com.techinwork.ggkush.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
