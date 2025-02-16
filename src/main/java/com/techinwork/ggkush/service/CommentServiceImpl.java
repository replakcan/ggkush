package com.techinwork.ggkush.service;

import com.techinwork.ggkush.entity.Comment;
import com.techinwork.ggkush.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    @Override
    public Comment findById(Long commentId) {
        Optional<Comment> commentOptional = this.commentRepository.findById(commentId);

        if (commentOptional.isPresent()) return commentOptional.get();
        throw new RuntimeException("Comment not found");
    }

    @Override
    public Comment delete(Long commentId) {
        Comment comment = this.findById(commentId);
        this.commentRepository.delete(comment);
        return comment;
    }

    @Override
    public Comment save( Comment comment) {
        this.commentRepository.save(comment);
        return comment;
    }
}
