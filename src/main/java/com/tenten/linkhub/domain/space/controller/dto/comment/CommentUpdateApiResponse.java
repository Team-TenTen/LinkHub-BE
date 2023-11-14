package com.tenten.linkhub.domain.space.controller.dto.comment;

public record CommentUpdateApiResponse(Long commentId) {
    public static CommentUpdateApiResponse from(Long updatedCommentId) {
        return new CommentUpdateApiResponse(updatedCommentId);
    }
}
