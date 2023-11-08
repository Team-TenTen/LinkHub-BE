package com.tenten.linkhub.domain.space.service.dto.comment;

public record CommentCreateRequest(
        Long spaceId,
        Long memberId,
        String content
) {
}
