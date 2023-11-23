package com.tenten.linkhub.domain.space.service.dto.link;

import org.springframework.data.domain.Pageable;

public record LinksGetByQueryRequest(
        Pageable pageable,
        Long spaceId,
        Long memberId,
        Long tagId
) {
}
