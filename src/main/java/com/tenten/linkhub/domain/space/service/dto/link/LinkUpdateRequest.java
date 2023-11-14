package com.tenten.linkhub.domain.space.service.dto.link;

public record LinkUpdateRequest(
        Long spaceId,
        String url,
        String title,
        String tag,
        Long memberId,
        Long linkId
) {
}
