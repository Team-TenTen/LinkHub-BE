package com.tenten.linkhub.domain.space.service.dto.comment;

public record RootCommentCreateRequest(
        Long spaceId,
        Long memberId,
        String content
) {
}
