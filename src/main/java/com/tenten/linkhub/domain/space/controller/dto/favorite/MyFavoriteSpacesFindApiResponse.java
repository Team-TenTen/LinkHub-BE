package com.tenten.linkhub.domain.space.controller.dto.favorite;

import com.tenten.linkhub.domain.space.model.category.Category;

public record MyFavoriteSpacesFindApiResponse(
        Long spaceId,
        String spaceName,
        String description,
        Category category,
        Long viewCount,
        Long scrapCount,
        Long favoriteCount,
        String spaceImagePath,
        String ownerNickName
) {
}
