package com.tenten.linkhub.domain.space.service.dto.link;

public record LinkCreateRequest(
        Long spaceId,
        String url,
        String title,
        String tag,
        Long memberId
) {
}
