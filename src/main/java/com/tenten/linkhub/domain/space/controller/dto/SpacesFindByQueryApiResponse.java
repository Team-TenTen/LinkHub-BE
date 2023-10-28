package com.tenten.linkhub.domain.space.controller.dto;

import com.tenten.linkhub.domain.space.model.category.Category;

public record SpacesFindByQueryApiResponse(
        Long spaceId,
        String spaceName,
        String description,
        Category category,
        Long viewCount,
        Long scrapCount,
        Long favoriteCount
) {
}
