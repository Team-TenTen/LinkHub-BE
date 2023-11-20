package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.space.model.category.Category;

public record MemberSpacesFindApiResponse(
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
