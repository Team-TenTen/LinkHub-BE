package com.tenten.linkhub.domain.space.controller.dto.comment;

public record RootCommentCreateApiResponse(Long commentId) {

    public static RootCommentCreateApiResponse from(Long commentId){
        return new RootCommentCreateApiResponse(commentId);
    }

}
