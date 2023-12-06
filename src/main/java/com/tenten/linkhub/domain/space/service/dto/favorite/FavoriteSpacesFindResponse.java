package com.tenten.linkhub.domain.space.service.dto.favorite;

import com.tenten.linkhub.domain.space.model.category.Category;

public record FavoriteSpacesFindResponse(
        Long spaceId,
        String spaceName,
        String description,
        Category category,
        Boolean isVisible,
        Boolean isComment,
        Boolean isLinkSummarizable,
        Boolean isReadMarkEnabled,
        Long viewCount,
        Long scrapCount,
        Long favoriteCount,
        String spaceImagePath,
        String ownerNickName
) {
}
