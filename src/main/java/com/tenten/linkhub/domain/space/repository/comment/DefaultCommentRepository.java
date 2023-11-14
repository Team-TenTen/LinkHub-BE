package com.tenten.linkhub.domain.space.repository.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import com.tenten.linkhub.domain.space.repository.comment.query.CommentQueryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultCommentRepository implements CommentRepository{

    private final CommentJpaRepository commentJpaRepository;
    private final CommentQueryRepository commentQueryRepository;

    public DefaultCommentRepository(CommentJpaRepository commentJpaRepository, CommentQueryRepository commentQueryRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.commentQueryRepository = commentQueryRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Slice<CommentAndChildCommentCount> findCommentAndChildCommentCountBySpaceId(Long spaceId, Pageable pageable) {
        return commentQueryRepository.findCommentAndChildCommentCountBySpaceId(spaceId, pageable);
    }

    @Override
    public Optional<Comment> findById(Long parentCommentId) {
        return commentJpaRepository.findById(parentCommentId);
    }

    @Override
    public void deleteById(Long commentId) {
        return commentJpaRepository.deleteById(commentId);
    }

}
