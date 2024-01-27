package com.tenten.linkhub.domain.space.repository.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import com.tenten.linkhub.domain.space.repository.comment.querydsl.CommentQueryDslRepository;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultCommentRepository implements CommentRepository{

    private final CommentJpaRepository commentJpaRepository;
    private final CommentQueryDslRepository commentQueryRepository;

    public DefaultCommentRepository(CommentJpaRepository commentJpaRepository, CommentQueryDslRepository commentQueryRepository) {
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
    public Comment getById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("댓글을 찾을 수 없습니다."));
    }

    @Override
    public Slice<Comment> findRepliesById(Long commentId, Pageable pageable) {
        return commentJpaRepository.findRepliesByParentCommentId(commentId, pageable);
    }

}
