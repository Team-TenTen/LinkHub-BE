package com.tenten.linkhub.domain.space.repository.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultCommentRepository implements CommentRepository{

    private final CommentJpaRepository commentJpaRepository;

    public DefaultCommentRepository(CommentJpaRepository commentJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

}
