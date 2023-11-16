package com.tenten.linkhub.domain.space.controller.dto.comment;

public record ReplyCreateApiResponse(Long commentId) {

    public static ReplyCreateApiResponse from(Long commentId){
        return new ReplyCreateApiResponse(commentId);
    }

}
