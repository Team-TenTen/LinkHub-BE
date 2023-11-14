package com.tenten.linkhub.domain.space.repository.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.space.model.space.Comment;

public record CommentAndChildCommentCount(Comment comment, Long childCommentCount) {

    @QueryProjection
    public CommentAndChildCommentCount(Comment comment, Long childCommentCount) {
        this.comment = comment;
        this.childCommentCount = childCommentCount;
    }

}
