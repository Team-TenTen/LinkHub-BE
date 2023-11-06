package com.tenten.linkhub.domain.space.service.dto.link;

import com.tenten.linkhub.domain.space.model.space.Space;

public record LinkCreateRequest(
        Space space,
        String url,
        String title,
        String tag,
        Long memberId
) {
}
