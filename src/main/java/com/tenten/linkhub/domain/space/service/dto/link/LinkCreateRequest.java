package com.tenten.linkhub.domain.space.service.dto.link;

import com.tenten.linkhub.domain.space.model.link.Color;

public record LinkCreateRequest(
        Long spaceId,
        String url,
        String title,
        String tag,
        Long memberId,
        Color color
) {
}
