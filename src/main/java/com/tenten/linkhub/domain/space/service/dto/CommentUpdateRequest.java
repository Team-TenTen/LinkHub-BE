package com.tenten.linkhub.domain.space.service.dto;

public record CommentUpdateRequest(
        Long spaceId,
        Long commentId,
        Long memberId,
        String content)
{

}
