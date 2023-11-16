package com.tenten.linkhub.domain.space.service.dto.comment;

public record ReplyCreateRequest(
        Long spaceId,
        Long commentId,
        Long memberId,
        String content
) {
}
