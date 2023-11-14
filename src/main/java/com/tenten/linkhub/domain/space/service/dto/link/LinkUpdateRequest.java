package com.tenten.linkhub.domain.space.service.dto.link;

import com.tenten.linkhub.domain.space.model.link.Color;

public record LinkUpdateRequest(
        Long spaceId,
        String url,
        String title,
        String tag,
        Long memberId,
        Long linkId,
        Color color
) {
}
