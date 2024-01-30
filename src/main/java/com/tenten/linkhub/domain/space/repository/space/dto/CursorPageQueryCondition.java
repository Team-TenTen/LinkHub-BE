package com.tenten.linkhub.domain.space.repository.space.dto;

import com.tenten.linkhub.domain.space.common.SpaceCursorPageRequest;

public record CursorPageQueryCondition(
        SpaceCursorPageRequest pageable,
        Long lastFavoriteCount,
        Long lastSpaceId
) {
}
