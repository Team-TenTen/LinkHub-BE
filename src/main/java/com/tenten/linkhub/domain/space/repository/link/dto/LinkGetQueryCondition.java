package com.tenten.linkhub.domain.space.repository.link.dto;

import org.springframework.data.domain.Pageable;

public record LinkGetQueryCondition(
        Long spaceId,
        Long memberId,
        Pageable pageable,
        Long tagId
) {
}

