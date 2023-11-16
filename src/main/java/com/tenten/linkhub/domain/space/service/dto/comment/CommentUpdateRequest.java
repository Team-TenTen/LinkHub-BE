package com.tenten.linkhub.domain.space.service.dto.comment;

public record CommentUpdateRequest(
        Long spaceId,
        Long commentId,
        Long memberId,
        String content) {

}
