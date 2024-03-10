package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.common.SpaceCursorPageRequest;

public record PublicSpacesFindWithFilterRequest(
        SpaceCursorPageRequest pageable,
        Long lastFavoriteCount,
        Long lastSpaceId
) {
}
