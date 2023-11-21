package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;

import java.util.List;

public record SpaceWithSpaceImageAndSpaceMemberInfo(
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
        String spaceImageName,
        Boolean isOwner,
        Boolean isCanEdit,
        Boolean hasFavorite,
        List<SpaceMemberInfo> spaceMemberInfos
) {
    public static SpaceWithSpaceImageAndSpaceMemberInfo of(Space space, List<SpaceMember> sortedSpaceMember, Boolean isOwner, Boolean isCanEdit, Boolean hasFavorite) {
        List<SpaceMemberInfo> spaceMemberInfos = sortedSpaceMember.stream()
                .map(sm -> new SpaceMemberInfo(sm.getMemberId(), sm.getRole()))
                .toList();

        return new SpaceWithSpaceImageAndSpaceMemberInfo(
                space.getId(),
                space.getSpaceName(),
                space.getDescription(),
                space.getCategory(),
                space.getIsVisible(),
                space.getIsComment(),
                space.getIsLinkSummarizable(),
                space.getIsReadMarkEnabled(),
                space.getViewCount(),
                space.getScrapCount(),
                space.getFavoriteCount(),
                space.getSpaceImages().isEmpty() ? null : space.getSpaceImages().get(0).getPath(),
                space.getSpaceImages().isEmpty() ? null : space.getSpaceImages().get(0).getName(),
                isOwner,
                isCanEdit,
                hasFavorite,
                spaceMemberInfos
        );
    }

}
