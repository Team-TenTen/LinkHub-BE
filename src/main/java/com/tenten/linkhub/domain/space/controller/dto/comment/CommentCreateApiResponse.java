package com.tenten.linkhub.domain.space.controller.dto.comment;

public record CommentCreateApiResponse(Long commentId) {

    public static CommentCreateApiResponse from(Long commentId){
        return new CommentCreateApiResponse(commentId);
    }

}
