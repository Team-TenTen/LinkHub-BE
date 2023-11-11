package com.tenten.linkhub.domain.space.service.dto.comment;

public record CommentAndChildCountDto(
        Long commentId,
        String content,
        Long memberId,
        Long childCount
) {
}
