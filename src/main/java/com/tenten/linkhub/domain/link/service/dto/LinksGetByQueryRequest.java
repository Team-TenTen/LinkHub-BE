package com.tenten.linkhub.domain.link.service.dto;

import org.springframework.data.domain.Pageable;

public record LinksGetByQueryRequest(
        Pageable pageable,
        Long spaceId,
        Long memberId,
        Long tagId
) {
}
